package com.kronostools.timehammer.banner.deployment;

import com.kronostools.timehammer.banner.BannerRecorder;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;

class BannerProcessor {

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    public void recordBanner(BannerRecorder recorder) {
        recorder.print();
    }
}