package com.xinguang.tubobo.impl.merchant.common;/**
 * Created by hanyong on 2017/1/14.
 */

import com.xinguang.tubobo.impl.merchant.service.SysSeqService;
import org.hibernate.MappingException;
import org.hibernate.id.IdentifierGeneratorHelper;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.id.enhanced.AccessCallback;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.id.enhanced.OptimizerFactory;

/**
 * @author hanyong
 * @Date 2017/1/14
 */
public class SeqGenerator {

    private Class returnedClass = Long.class;

    private String segmentValue;
    private int incrementSize;
    private int initialValue = 1;

    private Optimizer optimizer;

    private SysSeqService sysSeqService;


    private SeqGenerator(){}


    /**
     * generate a new SeqGenerator
     * @param segmentValue
     * @param incrementSize
     * @param sysSeqService
     * @return
     * @throws MappingException
     */
    public static SeqGenerator newInstance(String segmentValue, int incrementSize, SysSeqService sysSeqService) throws MappingException {
        SeqGenerator generator = new SeqGenerator();

        generator.segmentValue = segmentValue;
        generator.incrementSize = incrementSize;
        generator.sysSeqService = sysSeqService;

        final String optimizationStrategy = OptimizerFactory.StandardOptimizerDescriptor.POOLED.getExternalName();
        generator.optimizer = OptimizerFactory.buildOptimizer(
                optimizationStrategy,
                generator.returnedClass,
                generator.incrementSize,
                -1
        );
        return generator;
    }

    public synchronized Long generate() {
        return (Long) optimizer.generate(
                new AccessCallback() {
                    @Override
                    public IntegralDataTypeHolder getNextValue() {
                        IntegralDataTypeHolder value = IdentifierGeneratorHelper.getIntegralDataTypeHolder(returnedClass);
                        Long nextVal = null;
                        do {
                            nextVal = sysSeqService.nextVal(segmentValue, initialValue, incrementSize);

                        } while (nextVal == null);

                        return value.initialize(nextVal);
                    }
                }
        );
    }

}

