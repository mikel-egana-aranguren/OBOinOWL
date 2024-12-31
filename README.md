# OBOinOWL

Understanding and using the meaning of statements in a bio-ontology: recasting the Gene Ontology in OWL

The arguments of the shell program are: obo file, owl file, owl file logical URI. So for example if we
want to convert the Sequence Ontology from OBO to OWL:

java -jar obo2owl.jar ./ontologies/so.obo ./ontologies/so.owl http://www.gong.manchester.ac.uk/so.owl
