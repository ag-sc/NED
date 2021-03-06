/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.templates;

import de.citec.sc.corpus.Annotation;
import de.citec.sc.query.CandidateRetriever;
import de.citec.sc.query.CandidateRetrieverOnLucene;
import de.citec.sc.query.Instance;
import de.citec.sc.variables.State;
import factors.AbstractFactor;
import factors.impl.SingleVariableFactor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import learning.Vector;
import org.apache.logging.log4j.LogManager;
import test.TestSearch;
import utility.VariableID;

/**
 *
 * @author sherzod
 */
public class LuceneScoreTemplate extends templates.AbstractTemplate<State> {

    private static org.apache.logging.log4j.Logger log = LogManager.getFormatterLogger();

    CandidateRetriever indexSearch;

    public LuceneScoreTemplate(CandidateRetriever i) {
        indexSearch = i;
    }

    @Override
    protected Collection<AbstractFactor> generateFactors(State state) {
        Set<AbstractFactor> factors = new HashSet<>();
        for (VariableID entityID : state.getEntityIDs()) {
            factors.add(new SingleVariableFactor(this, entityID));
        }
        log.info("Generate %s factors for state %s.", factors.size(), state.getID());
        return factors;
    }

    @Override
    protected void computeFactor(State state, AbstractFactor absFactor) {
        if (absFactor instanceof SingleVariableFactor) {

            SingleVariableFactor factor = (SingleVariableFactor) absFactor;
            Annotation entity = state.getEntity(factor.entityID);

            Vector featureVector = new Vector();

            featureVector.set("Relative_TF", entity.getRelativeTermFrequencyScore());
            featureVector.set("Relative_PR", entity.getPageRankScore());
            featureVector.set("Relative String Similarity", entity.getStringSimilarity());
            
            //bins
////            for(double i=0.01; i<1.0; i=i+0.01){
////                featureVector.set("Relative_String_Similarity_HIGHER_THAN_"+i, entity.getStringSimilarity() > i ? 1.0 : 0);
////            }

            factor.setFeatures(featureVector);
        }
    }

}
