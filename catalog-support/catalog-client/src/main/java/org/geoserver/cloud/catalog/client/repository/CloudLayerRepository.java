/*
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */
package org.geoserver.cloud.catalog.client.repository;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.plugin.CatalogInfoRepository.LayerRepository;

public class CloudLayerRepository extends CatalogServiceClientRepository<LayerInfo>
        implements LayerRepository {

    private final @Getter Class<LayerInfo> contentType = LayerInfo.class;

    public @Override Stream<LayerInfo> findAllByDefaultStyleOrStyles(StyleInfo style) {
        return client().findLayersWithStyle(style.getId()).map(this::resolve).toStream();
    }

    public @Override Stream<LayerInfo> findAllByResource(ResourceInfo resource) {
        return client().findLayersByResourceId(resource.getId()).map(this::resolve).toStream();
    }

    public @Override Optional<LayerInfo> findOneByName(@NonNull String name) {
        return findFirstByName(name, LayerInfo.class);
    }
}
