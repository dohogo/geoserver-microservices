/*
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */
package org.geoserver.cloud.catalog.client.reactivefeign;

import feign.Contract;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import java.nio.ByteBuffer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.geoserver.cloud.catalog.client.reactivefeign.ReactiveResourceStoreClient.ReactiveResourceStoreFeignConfiguration;
import org.geoserver.platform.resource.Paths;
import org.geoserver.platform.resource.Resource;
import org.geoserver.platform.resource.Resource.Type;
import org.geoserver.platform.resource.ResourceStore;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/** Catalog-service client to support {@link ResourceStore} */
@ReactiveFeignClient( //
    name = "resources-client", //
    url = "${geoserver.backend.catalog-service.uri:catalog-service}", //
    path = "/api/v1/resources",
    fallbackFactory = ResourceStoreFallbackFactory.class,
    configuration = {ReactiveResourceStoreFeignConfiguration.class}
)
public interface ReactiveResourceStoreClient {

    @Data
    @NoArgsConstructor
    class ResourceDescriptor {
        private String path = Paths.BASE;
        private Resource.Type type = Type.UNDEFINED;
        private long lastModified;

        public static ResourceDescriptor valueOf(Resource resource) {
            ResourceDescriptor d = new ResourceDescriptor();
            d.setType(resource.getType());
            d.setPath(resource.path());
            d.setLastModified(resource.lastmodified());
            return d;
        }

        public void setPath(String path) {
            this.path = path == null ? Paths.BASE : path;
        }
    }

    @RequestLine(value = "GET /{path}", decodeSlash = false)
    @Headers({"Accept: application/json"})
    Mono<ResourceDescriptor> describe(@Param("path") String path);

    @RequestLine(value = "GET /{path}", decodeSlash = false)
    @Headers("Accept: application/stream+json")
    Flux<ResourceDescriptor> list(@Param("path") String path);

    @RequestLine(value = "GET /{path}", decodeSlash = false)
    @Headers("Accept: application/octet-stream")
    Mono<org.springframework.core.io.Resource> getFileContent(@Param("path") String path);

    @RequestLine(value = "PUT /{path}", decodeSlash = false)
    @Headers({"Content-Type: application/octet-stream", "Accept: application/json"})
    Mono<ResourceDescriptor> put(@Param("path") String path, ByteBuffer contents);

    @RequestLine(value = "POST /{path}", decodeSlash = false)
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Mono<ResourceDescriptor> create(@Param("path") String path, ResourceDescriptor resource);

    @RequestLine(value = "DELETE /{path}", decodeSlash = false)
    @Headers({"Accept: application/json"})
    Mono<Boolean> delete(@Param("path") String path);

    @RequestLine(value = "POST /move/{path}?to={target}", decodeSlash = false)
    @Headers("Accept: application/json")
    Mono<ResourceDescriptor> move(@Param("path") String path, @Param("target") String target);

    /**
     * {@code Configuration @Configuration} for the resource store client to use feign's default
     * {@link Contract} instead of spring default's {@link SpringMvcContract}, cause with the later
     * I couldn't find a way to use the same path with different {@code Accept:} headers that works
     */
    public static @Configuration class ReactiveResourceStoreFeignConfiguration {

        public @Bean Contract feignContract() {
            return new Contract.Default();
        }
    }
}
