/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.rendering.internal.transformation;

import org.junit.Test;
import org.xwiki.rendering.block.XDOM;
import org.xwiki.rendering.syntax.Syntax;
import org.xwiki.rendering.transformation.TransformationContext;
import org.xwiki.rendering.transformation.TransformationManager;
import org.xwiki.test.AbstractComponentTestCase;

/**
 * Unit tests for {@link DefaultTransformationManager}.
 * 
 * @version $Id$
 */
public class DefaultTransformationManagerTest extends AbstractComponentTestCase
{
    @Test
    public void testTransformations() throws Exception 
    {
        TransformationManager manager = getComponentManager().getInstance(TransformationManager.class);
        manager.performTransformations(XDOM.EMPTY, new TransformationContext(XDOM.EMPTY, Syntax.XWIKI_2_0));
    }
}
