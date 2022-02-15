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
package org.xwiki.rendering.listener.chaining;

import java.lang.reflect.Method;
import java.util.Map;

import org.xwiki.rendering.listener.Format;
import org.xwiki.rendering.listener.HeaderLevel;
import org.xwiki.rendering.listener.ListType;
import org.xwiki.rendering.listener.MetaData;
import org.xwiki.rendering.listener.reference.ResourceReference;
import org.xwiki.rendering.syntax.Syntax;

/**
 * Default and basic implementation of a chaining listener that knows how to delegate event calls to the next listener
 * in the chain.
 *
 * @version $Id$
 * @since 1.8RC1
 */
public abstract class AbstractChainingListener implements ChainingListener
{
    /**
     * The chain to use to know the next listener to call on events.
     */
    private ListenerChain listenerChain;

    /**
     * True if {@link #beginListItem(Map)} should redirect to {@link #beginListItem()} for retro compatibility.
     * <p>
     * {@link #beginListItem(Map)} was added long after {@link #beginListItem()} and various renderers/listeners
     * override {@link #beginListItem()} only and won't be called in post 10.0 versions of XWiki without this retro
     * compatibility trick.
     * 
     * @since 10.11.10
     * @since 11.3.7
     * @since 11.10.2
     * @since 12.0RC1
     */
    private final boolean beginListItemRetroCompatibility;

    /**
     * True if {@link #endListItem(Map)} should redirect to {@link #endListItem()} for retro compatibility.
     * <p>
     * {@link #endListItem(Map)} was added long after {@link #endListItem()} and various renderers/listeners
     * override {@link #endListItem()} only and won't be called in post 10.0 versions of XWiki without this retro
     * compatibility trick.
     *
     * @since 13.10.3
     * @since 14.1RC1
     */
    private final boolean endListItemRetroCompatibility;

    /**
     * The default constructor.
     * <p>
     * Initialize {@link #beginListItemRetroCompatibility} field.
     */
    public AbstractChainingListener()
    {
        this.beginListItemRetroCompatibility = needsRetroCompatibilityWithoutParameters("beginListItem");
        this.endListItemRetroCompatibility = needsRetroCompatibilityWithoutParameters("endListItem");
    }

    /**
     * @param methodName The method name to check for retro compatibility without parameters.
     * @return If the method is only implemented without parameters and not with parameters in a descendant class.
     * @since 13.10.3
     * @since 14.1RC1
     */
    private boolean needsRetroCompatibilityWithoutParameters(String methodName)
    {
        for (Class<?> current = getClass(); current != AbstractChainingListener.class; current =
            current.getSuperclass()) {
            boolean withParameter = false;
            boolean withoutParameter = false;

            // Search for beginListItem methods
            for (Method method : current.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    if (method.getParameterCount() == 0) {
                        withoutParameter = true;
                    } else {
                        withParameter = true;
                    }
                }
            }

            // Enable the retro compatibility flag if #beginListItem() is overwritten but not #beginListItem(Map)
            if (!withParameter && withoutParameter) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param listenerChain see {@link #getListenerChain()}
     * @since 2.0M3
     */
    public void setListenerChain(ListenerChain listenerChain)
    {
        this.listenerChain = listenerChain;
    }

    @Override
    public ListenerChain getListenerChain()
    {
        return this.listenerChain;
    }

    @Override
    public void beginDefinitionDescription()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginDefinitionDescription();
        }
    }

    @Override
    public void beginDefinitionList(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginDefinitionList(parameters);
        }
    }

    @Override
    public void beginDefinitionTerm()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginDefinitionTerm();
        }
    }

    @Override
    public void beginDocument(MetaData metadata)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginDocument(metadata);
        }
    }

    @Override
    public void beginGroup(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginGroup(parameters);
        }
    }

    @Override
    public void beginFormat(Format format, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginFormat(format, parameters);
        }
    }

    @Override
    public void beginHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginHeader(level, id, parameters);
        }
    }

    @Override
    public void beginLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginLink(reference, freestanding, parameters);
        }
    }

    @Override
    public void beginList(ListType type, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginList(type, parameters);
        }
    }

    @Override
    public void beginListItem()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginListItem();
        }
    }

    @Override
    public void beginListItem(Map<String, String> parameters)
    {
        // Make sure to call the old beginListItem() if it's the only thing implemented by the extending class since
        // this new one existed only after 10.10
        if (this.beginListItemRetroCompatibility) {
            beginListItem();
        } else {
            ChainingListener next = getListenerChain().getNextListener(getClass());
            if (next != null) {
                next.beginListItem(parameters);
            }
        }
    }

    @Override
    public void beginMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginMacroMarker(name, parameters, content, isInline);
        }
    }

    @Override
    public void beginParagraph(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginParagraph(parameters);
        }
    }

    @Override
    public void beginQuotation(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginQuotation(parameters);
        }
    }

    @Override
    public void beginQuotationLine()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginQuotationLine();
        }
    }

    @Override
    public void beginSection(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginSection(parameters);
        }
    }

    @Override
    public void beginTable(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginTable(parameters);
        }
    }

    @Override
    public void beginTableCell(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginTableCell(parameters);
        }
    }

    @Override
    public void beginTableHeadCell(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginTableHeadCell(parameters);
        }
    }

    @Override
    public void beginTableRow(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginTableRow(parameters);
        }
    }

    @Override
    public void beginMetaData(MetaData metadata)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginMetaData(metadata);
        }
    }

    @Override
    public void beginFigure(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginFigure(parameters);
        }
    }

    @Override
    public void beginFigureCaption(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.beginFigureCaption(parameters);
        }
    }

    @Override
    public void endDefinitionDescription()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endDefinitionDescription();
        }
    }

    @Override
    public void endDefinitionList(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endDefinitionList(parameters);
        }
    }

    @Override
    public void endDefinitionTerm()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endDefinitionTerm();
        }
    }

    @Override
    public void endDocument(MetaData metadata)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endDocument(metadata);
        }
    }

    @Override
    public void endGroup(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endGroup(parameters);
        }
    }

    @Override
    public void endFormat(Format format, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endFormat(format, parameters);
        }
    }

    @Override
    public void endHeader(HeaderLevel level, String id, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endHeader(level, id, parameters);
        }
    }

    @Override
    public void endLink(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endLink(reference, freestanding, parameters);
        }
    }

    @Override
    public void endList(ListType type, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endList(type, parameters);
        }
    }

    @Override
    public void endListItem()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endListItem();
        }
    }

    @Override
    public void endListItem(Map<String, String> parameters)
    {
        // Make sure to call the old endListItem() if it's the only thing implemented by the extending class since
        // this new one existed only after 10.10.
        if (this.endListItemRetroCompatibility) {
            endListItem();
        } else {
            ChainingListener next = getListenerChain().getNextListener(getClass());
            if (next != null) {
                next.endListItem(parameters);
            }
        }
    }

    @Override
    public void endMacroMarker(String name, Map<String, String> parameters, String content, boolean isInline)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endMacroMarker(name, parameters, content, isInline);
        }
    }

    @Override
    public void endParagraph(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endParagraph(parameters);
        }
    }

    @Override
    public void endQuotation(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endQuotation(parameters);
        }
    }

    @Override
    public void endQuotationLine()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endQuotationLine();
        }
    }

    @Override
    public void endSection(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endSection(parameters);
        }
    }

    @Override
    public void endTable(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endTable(parameters);
        }
    }

    @Override
    public void endTableCell(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endTableCell(parameters);
        }
    }

    @Override
    public void endTableHeadCell(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endTableHeadCell(parameters);
        }
    }

    @Override
    public void endTableRow(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endTableRow(parameters);
        }
    }

    @Override
    public void endMetaData(MetaData metadata)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endMetaData(metadata);
        }
    }

    @Override
    public void endFigure(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endFigure(parameters);
        }
    }

    @Override
    public void endFigureCaption(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.endFigureCaption(parameters);
        }
    }

    @Override
    public void onEmptyLines(int count)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onEmptyLines(count);
        }
    }

    @Override
    public void onHorizontalLine(Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onHorizontalLine(parameters);
        }
    }

    @Override
    public void onId(String name)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onId(name);
        }
    }

    @Override
    public void onImage(ResourceReference reference, boolean freestanding, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onImage(reference, freestanding, parameters);
        }
    }

    @Override
    public void onMacro(String id, Map<String, String> parameters, String content, boolean inline)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onMacro(id, parameters, content, inline);
        }
    }

    @Override
    public void onNewLine()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onNewLine();
        }
    }

    @Override
    public void onSpace()
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onSpace();
        }
    }

    @Override
    public void onSpecialSymbol(char symbol)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onSpecialSymbol(symbol);
        }
    }

    @Override
    public void onVerbatim(String content, boolean inline, Map<String, String> parameters)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onVerbatim(content, inline, parameters);
        }
    }

    @Override
    public void onWord(String word)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onWord(word);
        }
    }

    @Override
    public void onRawText(String text, Syntax syntax)
    {
        ChainingListener next = getListenerChain().getNextListener(getClass());
        if (next != null) {
            next.onRawText(text, syntax);
        }
    }
}
