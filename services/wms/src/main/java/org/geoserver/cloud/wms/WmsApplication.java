/* (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.cloud.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WmsApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(WmsApplication.class, args);
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
