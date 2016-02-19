/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.citec.sc.createIndex;

import de.citec.sc.index.AnchorTextLoader;

/**
 *
 * @author sherzod
 */
public class CreateIndexes {

	public static void main(String[] args) {
		run();
	}

	public static void run() {
		System.out.println("Creating index files ...");

		// SurfaceFormsDBpedia dbpediaLoader = new
		// SurfaceFormsDBpedia("propList3.txt");
		// System.out.println("dbpediaIndexAll ...");
		// dbpediaLoader.load("dbpediaFiles/");

		AnchorTextLoader anchorLoader = new AnchorTextLoader();
		anchorLoader.load(true, "anchorIndex", "anchorFiles/");

		anchorLoader.load(true, "dbpediaIndex", "dbpediaFiles/");

		System.out.println("DONE.");
	}
}
