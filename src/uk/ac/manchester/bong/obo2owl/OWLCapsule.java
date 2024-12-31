/**
 * 
 * Copyright © Mikel Egana Aranguren 
 * The OWLCapsule.java software is free software and is licensed under the terms of the 
 * GNU General Public License (GPL) as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version. The OWLCapsule.java 
 * software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GPL for more details; a copy of the GPL is included with this product. 
 * 
 * For more info:
 * mikel.eganaaranguren@cs.manchester.ac.uk
 * http://www.gong.manchester.ac.uk
 * 
 */

package uk.ac.manchester.bong.obo2owl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.semanticweb.owl.impl.model.OWLConnectionImpl;
import org.semanticweb.owl.io.RendererException;
import org.semanticweb.owl.io.vocabulary.XMLSchemaSimpleDatatypeVocabulary;
import org.coode.owl.io.rdfxml.RDFXMLRenderer;
import org.semanticweb.owl.model.OWLAnnotationProperty;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataValue;
import org.semanticweb.owl.model.OWLException;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.change.AddAnnotationInstance;
import org.semanticweb.owl.model.change.AddEntity;
import org.semanticweb.owl.model.change.ChangeVisitor;
import org.semanticweb.owl.model.change.OntologyChange;
import org.semanticweb.owl.util.OWLConnection;

/**
 * This is a little wrapper for the most used operations in the OWL API.
 */

public class OWLCapsule{
	private OWLOntology ontology;
	private OWLConnection connection;
	private OWLDataFactory factory;
	
	public OWLCapsule (String OntologyURI) throws OWLException, URISyntaxException{
		this.connection = new OWLConnectionImpl();
		this.ontology = connection.createOntology(new URI(OntologyURI),new URI(OntologyURI));
		this.factory = connection.getDataFactory();
	}
	
	public OWLOntology getOntology (){
		return ontology;	
	}
	
	public OWLConnection getConnection (){
		return connection;
	}
	public OWLDataFactory getFactory (){
		return factory;
	}	
	public void writeOntology (String fileName) throws RendererException, IOException{
		File file = new File(fileName);
		OutputStream os = new BufferedOutputStream (new FileOutputStream (file));
		RDFXMLRenderer renderer = new RDFXMLRenderer ();
		renderer.renderOntology(ontology, new OutputStreamWriter(os));
		os.flush();
		os.close();
	}
	public OWLAnnotationProperty createOWLAnnotatioProperty (String propname){
		OWLAnnotationProperty annotprop = null;
		try {
			annotprop = factory.getOWLAnnotationProperty(new URI (ontology.getLogicalURI() + "#" + propname));
			ChangeVisitor annotvisitor = connection.getChangeVisitor(ontology);
			OntologyChange ocannot = new AddEntity(ontology, annotprop, null); 
			ocannot.accept(annotvisitor);
		} catch (OWLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return annotprop;
	}
	
	public OWLAnnotationProperty createOWLRDFSAnnotatioProperty (String propname){
		OWLAnnotationProperty annotprop = null;
		try {
			annotprop = factory.getOWLAnnotationProperty(new URI ("http://www.w3.org/2000/01/rdf-schema#" + propname));
			ChangeVisitor annotvisitor = connection.getChangeVisitor(ontology);
			OntologyChange ocannot = new AddEntity(ontology, annotprop, null); 
			ocannot.accept(annotvisitor);
		} catch (OWLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return annotprop;
	}
	
	public void addAnnotationValue (String classname, OWLAnnotationProperty annotprop, String annotvalue){
		try {
			OWLClass clazz = ontology.getClass(new URI(ontology.getLogicalURI() + "#" + classname));
			ChangeVisitor visitorAnn = connection.getChangeVisitor(ontology);
			String stringURIstr = (XMLSchemaSimpleDatatypeVocabulary.INSTANCE).getString();
			URI stringURI = new URI (stringURIstr);
			OWLDataValue annotdefvalue = factory.getOWLConcreteData(stringURI, "en", annotvalue); 
			OntologyChange ocAnnotation = new AddAnnotationInstance(ontology, clazz, annotprop, annotdefvalue, null);
			ocAnnotation.accept(visitorAnn);
		} catch (OWLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	public void addOntologyAnnotationValue (OWLAnnotationProperty annotprop, String annotvalue){
		try {
			ChangeVisitor visitorAnn = connection.getChangeVisitor(ontology);
			String stringURIstr = (XMLSchemaSimpleDatatypeVocabulary.INSTANCE).getString();
			URI stringURI = new URI (stringURIstr);
			OWLDataValue annotdefvalue = factory.getOWLConcreteData(stringURI, "en", annotvalue); 
			OntologyChange ocAnnotation = new AddAnnotationInstance(ontology, ontology, annotprop, annotdefvalue, null);
			ocAnnotation.accept(visitorAnn);
		} catch (OWLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
