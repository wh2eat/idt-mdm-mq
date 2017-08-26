package com.idata.mq.base.util;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

public class RandomGeneratorUtil {
    private static TimeBasedGenerator randomGenerator = Generators.timeBasedGenerator();

    public static TimeBasedGenerator getTimeBasedGenerator() {
        return RandomGeneratorUtil.randomGenerator;
    }
}
