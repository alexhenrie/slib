/*

 Copyright or © or Copr. Ecole des Mines d'Alès (2012) 

 This software is a computer program whose purpose is to 
 process semantic graphs.

 This software is governed by the CeCILL  license under French law and
 abiding by the rules of distribution of free software.  You can  use, 
 modify and/ or redistribute the software under the terms of the CeCILL
 license as circulated by CEA, CNRS and INRIA at the following URL
 "http://www.cecill.info". 

 As a counterpart to the access to the source code and  rights to copy,
 modify and redistribute granted by the license, users are provided only
 with a limited warranty  and the software's author,  the holder of the
 economic rights,  and the successive licensors  have only  limited
 liability. 

 In this respect, the user's attention is drawn to the risks associated
 with loading,  using,  modifying and/or developing or reproducing the
 software by the user in light of its specific status of free software,
 that may mean  that it is complicated to manipulate,  and  that  also
 therefore means  that it is reserved for developers  and  experienced
 professionals having in-depth computer knowledge. Users are therefore
 encouraged to load and test the software's suitability as regards their
 requirements in conditions enabling the security of their systems and/or 
 data to be ensured and,  more generally, to use and operate it in the 
 same conditions as regards security. 

 The fact that you are presently reading this means that you have had
 knowledge of the CeCILL license and that you accept its terms.

 */
package slib.sglib.model.impl.repo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.openrdf.model.URI;
import org.openrdf.model.vocabulary.RDFS;
import slib.sglib.model.repo.DataFactory;
import slib.sglib.model.repo.PredicateFactory;
import slib.sglib.model.impl.voc.SLIBVOC;
import slib.utils.ex.SLIB_Ex_Critic;
import slib.utils.ex.SLIB_Ex_Warning;

/**
 *
 * @author seb
 */
public class PredicateURIRepo implements PredicateFactory {

    private static PredicateURIRepo singleton;
    private DataFactory factory;
    private Set<URI> pURIs; // predicate URIs 
    private Map<URI, URI> inverses;


    private PredicateURIRepo(DataFactory factory) {

        this.factory = factory;
        pURIs    = new HashSet<URI>();
        inverses = new HashMap<URI, URI>();

        init();
    }

    private void init() {
        pURIs.add(RDFS.SUBCLASSOF);
    }

    /**
     *
     * @param factory
     * @return
     */
    protected static PredicateURIRepo getInstance(DataFactory factory) {
        if (singleton == null) {
            singleton = new PredicateURIRepo(factory);
        }
        return singleton;
    }

    /**
     *
     * @param uri
     * @return
     */
    public boolean contains(URI uri) {
        return pURIs.contains(uri);
    }

    /**
     *
     * @param suri
     * @return
     * @throws SLIB_Ex_Critic
     */
    public URI load(String suri) throws SLIB_Ex_Critic {

        URI uri = factory.createURI(suri);

        if (!pURIs.contains(uri)) {
            pURIs.add(uri);
        }

        return uri;
    }

    /**
     *
     * @param uri
     * @return
     */
    public URI getInverseURI(URI uri) {
        return inverses.get(uri);
    }

    /**
     *
     * @param uri
     * @param uriI
     * @throws SLIB_Ex_Warning
     */
    public void defineInverseURI(URI uri, URI uriI) throws SLIB_Ex_Warning {


        if (inverses.containsKey(uri)
                && !inverses.get(uri).equals(uriI)) {
            throw new SLIB_Ex_Warning(""
                    + "Error Setting " + uriI + " as inverse of " + uri + ""
                    + "\nInverse URI already define for " + uri + "-> " + inverses.get(uri));
        }

        if (inverses.containsKey(uriI)
                && !inverses.get(uriI).equals(uri)) {
            throw new SLIB_Ex_Warning(""
                    + "Error Setting " + uri + " as inverse of " + uriI + ""
                    + "\nInverse URI already define for " + uriI + "-> " + inverses.get(uriI));
        }

        inverses.put(uri, uriI);
        inverses.put(uriI, uri);
    }

    /**
     *
     * @param type
     * @return
     */
    public URI getInverse(URI type) {
        URI inverse = inverses.get(type);
        return inverse;
    }

    /**
     *
     * @param types
     * @return
     */
    public Set<URI> getInverse(Set<URI> types) {

        Set<URI> inverse = new HashSet<URI>();

        for (URI type : types) {
            inverse.add(getInverse(type));
        }

        return inverse;
    }

    /**
     *
     * @param eType
     * @return
     * @throws SLIB_Ex_Critic
     */
    public URI createInverse(URI eType) throws SLIB_Ex_Critic {
        URI inverse = load(SLIBVOC.SLIB_NS + eType.getLocalName() + "_inverse");
        return inverse;
    }

    /**
     * DEBUG
     *
     */
    public void showInverseMapping() {
        HashSet<URI> t = new HashSet<URI>();// to avoid duplicate
        for (Entry<URI, URI> e : inverses.entrySet()) {

            if (!t.contains(e.getKey()) && !t.contains(e.getKey())) {
                System.out.println(e.getKey() + "  " + e.getValue());

                t.add(e.getKey());
                t.add(e.getValue());
            }
        }
    }

    public URI createPURI(String uri) {
        URI u = factory.createURI(uri);
        pURIs.add(u);
        return u;

    }

    public boolean add(URI uri) {
        return pURIs.add(uri);
    }

    public Set<URI> getURIs() {
        return pURIs;
    }
}
