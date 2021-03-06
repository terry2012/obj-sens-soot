/* Soot - a J*va Optimization Framework
 * Copyright (C) 2002 Ondrej Lhotak
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package soot.jimple.toolkits.callgraph;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import soot.EntryPoints;
import soot.G;
import soot.Local;
import soot.MethodContext;
import soot.MethodOrMethodContext;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.Scene;
import soot.SootMethod;
import soot.Type;
import soot.jimple.spark.pag.NoContext;
import soot.jimple.spark.pag.ObjectSensitiveAllocNode;
import soot.jimple.spark.pag.ObjectSensitiveConfig;
import soot.jimple.spark.pag.PAG;
import soot.util.queue.QueueReader;

/** Models the call graph.
 * @author Ondrej Lhotak
 */
public final class CallGraphBuilder
{ 
    private PAG pa;
    private final ReachableMethods reachables;
    private final OnFlyCallGraphBuilder ofcgb;
    private final CallGraph cg;

    public CallGraph getCallGraph() { return cg; }
    public ReachableMethods reachables() { return reachables; }

    public static ContextManager makeContextManager( CallGraph cg, PAG pag ) {
        if (ObjectSensitiveConfig.isObjectSensitive() && ObjectSensitiveConfig.v().k() > 0)
            return new ObjSensContextManager(cg, pag);
        else
            return new ContextInsensitiveContextManager( cg );
    }

    
    /** This constructor builds a complete call graph using the given
     * PointsToAnalysis to resolve virtual calls. */
    public CallGraphBuilder( PAG pa ) {
        this.pa = pa;
        cg = new CallGraph();
        Scene.v().setCallGraph( cg );
        
        //create a no-context version of entry point list
        if (ObjectSensitiveConfig.isObjectSensitive())
            throw new RuntimeException("Does not work with object sensitivity!");
        
        reachables = Scene.v().getReachableMethods();
        ContextManager cm = makeContextManager(cg, pa);
        ofcgb = new OnFlyCallGraphBuilder( pa, cm, reachables );
   }
    /** This constructor builds the incomplete hack call graph for the
     * Dava ThrowFinder.
     * It uses all application class methods as entry points, and it ignores
     * any calls by non-application class methods.
     * Don't use this constructor if you need a real call graph. */
    public CallGraphBuilder() {
        G.v().out.println( "Warning: using incomplete callgraph containing "+
                "only application classes." );
        
        cg = new CallGraph();
        Scene.v().setCallGraph(cg);
        List<MethodOrMethodContext> entryPoints = new ArrayList<MethodOrMethodContext>();
        entryPoints.addAll( EntryPoints.v().methodsOfApplicationClasses() );
        entryPoints.addAll( EntryPoints.v().implicit() );
        reachables = new ReachableMethods( cg, entryPoints );
        ContextManager cm = new ContextInsensitiveContextManager( cg );
        ofcgb = new OnFlyCallGraphBuilder( cm, reachables, true );
    }
    public void build() {
        //not called for object sensitivity
       throw new RuntimeException("Should not be called by DroidSafe!");
    }
}

