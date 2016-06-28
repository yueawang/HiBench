/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intel.hibench.streambench.storm.micro;

import com.intel.hibench.streambench.storm.topologies.SingleSpoutTops;
import com.intel.hibench.streambench.storm.util.StormBenchConfig;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class GrepStream extends SingleSpoutTops {

  public GrepStream(StormBenchConfig config) {
    super(config);
  }

  @Override
  public void setBolts(TopologyBuilder builder) {
    builder.setBolt("grepAndPrint", new GrepBolt(config.pattern), config.boltThreads)
            .shuffleGrouping("spout");
  }

  private static class GrepBolt extends BaseBasicBolt {
    private String pattern;

    public GrepBolt(String p) {
      pattern = p;
    }

    public void execute(Tuple tuple, BasicOutputCollector collector) {
      String val = tuple.getString(0);
      if (val.contains(pattern)) {
        collector.emit(new Values(val));
      }
    }

    public void declareOutputFields(OutputFieldsDeclarer ofd) {
    }
  }

}
