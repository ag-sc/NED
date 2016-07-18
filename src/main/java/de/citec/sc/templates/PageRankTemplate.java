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
        
        if (useBins) {
            for (double i = 0.01; i < 1.0; i = i + 0.01) {
                featureVector.set("Relative_PR_bin_" + i, entity.getPageRankScore() > i ? 1.0 : 0);
            }
        }
        else{
            featureVector.set("Relative_PR", entity.getPageRankScore());
        }
        
    }

}
