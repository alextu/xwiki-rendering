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
package org.xwiki.rendering.internal.transformation.macro;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import org.xwiki.component.annotation.Component;
import org.xwiki.rendering.block.Block;
import org.xwiki.rendering.block.FormatBlock;
import org.xwiki.rendering.block.WordBlock;
import org.xwiki.rendering.block.match.ClassBlockMatcher;
import org.xwiki.rendering.listener.Format;
import org.xwiki.rendering.macro.AbstractNoParameterMacro;
import org.xwiki.rendering.transformation.MacroTransformationContext;

@Component
@Named("testformatmacro")
@Singleton
public class TestFormatMacro extends AbstractNoParameterMacro
{
    public TestFormatMacro()
    {
        super("Format Macro");
        setDefaultCategory("Test");
    }

    @Override
    public boolean supportsInlineMode()
    {
        return true;
    }

    @Override
    public List<Block> execute(Object parameters, String content, MacroTransformationContext context)
    {
        int wordCount = context.getXDOM().getBlocks(
            new ClassBlockMatcher(WordBlock.class), Block.Axes.DESCENDANT).size();
        return Arrays.<Block>asList(new FormatBlock(Arrays.<Block>asList(
            new WordBlock("formatmacro" + wordCount)), Format.NONE, Collections.singletonMap("param", "value")));
    }
}
