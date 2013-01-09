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
 
 
package slib.sml.sm.core.measures.graph.pairwise.dag.edge_based;

import java.util.Map;

import slib.sglib.model.graph.elements.V;
import slib.sml.sm.core.utils.SM_Engine;
import slib.sml.sm.core.utils.SMconf;
import slib.utils.ex.SLIB_Ex_Critic;



/**
 *
 * @author seb
 */
public class Sim_pairwise_DAG_edge_Pekar_Staab_2002 extends Sim_DAG_edge_abstract{

    /**
     *
     * @param a
     * @param b
     * @param c
     * @param conf
     * @return
     * @throws SLIB_Ex_Critic
     */
    public double sim(V a, V b, SM_Engine c, SMconf conf) throws SLIB_Ex_Critic {
	
	V msa  = c.getMSA(a,b);
	V root = c.getRoot();
	
	
//	System.out.println("MSA: "+msa);
//	System.out.println("ROOT: "+root);
	
	Map<V, Double> allSpMsa = c.getAllShortestPath(msa);
	
	
	return sim(allSpMsa, root, a, b);
}


	/**
     *
     * @param allSpMsa
     * @param root
     * @param a
     * @param b
     * @return
     */
    public double sim(Map<V, Double> allSpMsa, V root, V a, V b){
			
		double sp_mrca_root = allSpMsa.get(root);
		double sp_a_mrca 	= allSpMsa.get(a);
		double sp_b_mrca 	= allSpMsa.get(b);
		
		double den = sp_mrca_root + sp_a_mrca + sp_b_mrca;
		
		double sim = 0;
		
		if(den == 0) // root versus root
			sim = 1;
		else
			sim = sp_mrca_root / den  ;
		
		return sim;
	}
}
