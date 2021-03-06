/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
 *******************************************************************************/
package org.richfaces.tests.showcase.list.page;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.page.fragments.impl.list.ListComponent;
import org.richfaces.tests.page.fragments.impl.list.ListItem;
import org.richfaces.tests.page.fragments.impl.list.RichFacesList;
import org.richfaces.ui.iteration.list.ListType;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ListsPage {

    @FindBy(css = "[id$='list']")
    private RichFacesList list;
    @FindByJQuery("a:contains('ordered')")
    private WebElement orderedList;
    @FindByJQuery("a:contains('unordered')")
    private WebElement unordered;
    @FindByJQuery("a:contains('definitions')")
    private WebElement definitions;

    public void setType(ListType type) {
        switch (type) {
            case definitions:
                Graphene.guardAjax(definitions).click();
                break;
            case unordered:
                Graphene.guardAjax(unordered).click();
                break;
            case ordered:
                Graphene.guardAjax(orderedList).click();
                break;
        }
    }

    public ListComponent<? extends ListItem> getList() {
        return list;
    }
}
