/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.templates;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;

import de.citec.sc.corpus.Annotation;
import de.citec.sc.corpus.Document;
import de.citec.sc.variables.State;
import factors.Factor;
import factors.patterns.SingleVariablePattern;
import learning.Vector;

/**
 *
 * @author sherzod
 */
public class PageRankTemplate extends templates.AbstractTemplate<Document, State, SingleVariablePattern<Annotation>> {

    private static org.apache.logging.log4j.Logger log = LogManager.getFormatterLogger();
    private boolean useBins = false;
    final private static int NUMBER_OF_BINS = 1000;

    private static double[] bins = new double[NUMBER_OF_BINS + 1];

    static {

        for (int i = 0; i <= NUMBER_OF_BINS; i++) {
            bins[i] = (double) i / (double) NUMBER_OF_BINS;
        }

    }

    public PageRankTemplate(boolean useBins) {
        this.useBins = useBins;
    }

    @Override
    public Set<SingleVariablePattern<Annotation>> generateFactorPatterns(State state) {
        Set<SingleVariablePattern<Annotation>> factors = new HashSet<>();
        for (Annotation a : state.getEntities()) {
            factors.add(new SingleVariablePattern<>(this, a));
        }
        log.info("Generate %s factor patterns for state %s.", factors.size(), state.getID());
        return factors;
    }

    @Override
    public void computeFactor(Document instance, Factor<SingleVariablePattern<Annotation>> factor) {
        Annotation entity = factor.getFactorPattern().getVariable();
        log.debug("Compute %s factor for variable %s", PageRankTemplate.class.getSimpleName(), entity);
        Vector featureVector = factor.getFeatureVector();

        final int bin = getBin(entity.getPageRankScore());

        if (useBins) {
            for (int i = 0; i < bin; i++) {
                featureVector.set("Relative_PR_bin_ >= " + i, entity.getPageRankScore());
            }

            featureVector.set("1Relative_PR_binInBin_" + bin, 1d);
            featureVector.set("ScoreRelative_PR_binInBin_" + bin, entity.getPageRankScore());
        }
        else{
            featureVector.set("Relative_PR", entity.getPageRankScore());
        }

    }

    private int getBin(final double score) {
        for (int i = 0; i < bins.length - 1; i++) {
            if (bins[i] <= score && score < bins[i + 1]) {
                return i;
            }
        }
        return -1;
    }

}
