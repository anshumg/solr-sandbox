/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.update.processor;

import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.crossdc.KafkaMirroringSink;
import org.apache.solr.crossdc.MirroringException;
import org.apache.solr.crossdc.common.KafkaCrossDcConf;
import org.apache.solr.crossdc.common.MirroredSolrRequest;

import java.util.concurrent.TimeUnit;

public class KafkaRequestMirroringHandler implements RequestMirroringHandler {
    final KafkaCrossDcConf conf;
    final KafkaMirroringSink sink;

    public KafkaRequestMirroringHandler() {
        // TODO: Setup Kafka properly
        final String topicName = System.getProperty("topicname");
        final String boostrapServers = System.getProperty("bootstrapservers");
        conf = new KafkaCrossDcConf(boostrapServers, topicName, false, null);
        sink = new KafkaMirroringSink(conf);
    }

    /**
     * When called, should handle submitting the request to the queue
     *
     * @param request
     */
    @Override
    public void mirror(UpdateRequest request) throws MirroringException {
            // TODO: Enforce external version constraint for consistent update replication (cross-cluster)
            sink.submit(new MirroredSolrRequest(1, request, TimeUnit.MILLISECONDS.toNanos(
                    System.currentTimeMillis())));
    }
}