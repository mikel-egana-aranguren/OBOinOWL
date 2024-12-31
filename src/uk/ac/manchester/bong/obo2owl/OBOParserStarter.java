package uk.ac.manchester.bong.obo2owl;

import java.io.IOException;
import java.net.URISyntaxException;

import org.semanticweb.owl.model.OWLException;

import org.geneontology.oboedit.dataadapter.GOBOParseEngine;
import org.geneontology.oboedit.dataadapter.GOBOParseException;

public class OBOParserStarter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			// Settings
			String OBOFile = args [0]; //"/Users/pik/Desktop/obo/so.obo"
			String OWLFile = args [1]; //"/Users/pik/Desktop/obo/so.owl"
			String owlOntologyLogicalURI = args [2]; //"http://www.gong.manchester.ac.uk/so.owl"

			// PARSING is done twice: first all the terms and typedefs are included, 
			// then the structure is created
	
			// Create a new ontology (new owlcapsule)
			OWLCapsule owlcapsule = new OWLCapsule(owlOntologyLogicalURI);
			
			// Do the first round of parsing
			OBOParser parser = new OBOParser(owlcapsule,true);
			GOBOParseEngine engine = new GOBOParseEngine(parser);
			engine.setPath(OBOFile);
			engine.parse();
			
			// Do the second round of parsing
			OBOParser secondparser = new OBOParser(owlcapsule,false);
			GOBOParseEngine secondengine = new GOBOParseEngine(secondparser);
			secondengine.setPath(OBOFile);
			secondengine.parse();
			
			// Write the ontology to disk
			owlcapsule.writeOntology(OWLFile);
		} catch (OWLException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GOBOParseException e) {
			e.printStackTrace();
		}

	}

}
