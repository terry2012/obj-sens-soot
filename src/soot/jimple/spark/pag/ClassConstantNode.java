/* Soot - a J*va Optimization Framework
 * Copyright (C) 2003 Ondrej Lhotak
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

package soot.jimple.spark.pag;
import soot.Context;
import soot.RefType;
import soot.jimple.ClassConstant;
import soot.jimple.toolkits.pta.IClassConstantNode;

/** Represents an allocation site node the represents a known java.lang.Class
 * object.
 * @author Ondrej Lhotak
 */
public class ClassConstantNode extends InsensitiveAllocNode 
    implements IClassConstantNode, ContextElement { // (LWG) implements IClassConstantNode
    
    public String toString() {
	return "ClassConstantNode "+getNumber()+" "+newExpr;
    }
    public ClassConstant getClassConstant() {
        return (ClassConstant) newExpr;
    }

    /* End of public methods. */

    // LWG: changed from public access to package access
    ClassConstantNode( PAG pag, ClassConstant cc ) {
        super( pag, cc, RefType.v( "java.lang.Class" ), null );
        pag.getAllocNodeNumberer().add( this );
    }
}

