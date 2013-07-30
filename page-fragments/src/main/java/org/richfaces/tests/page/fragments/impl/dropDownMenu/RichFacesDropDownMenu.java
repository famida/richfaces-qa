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
package org.richfaces.tests.page.fragments.impl.dropDownMenu;

import java.util.List;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.contextMenu.AbstractPopupMenu;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class RichFacesDropDownMenu extends AbstractPopupMenu {

    @FindBy(className = "rf-ddm-itm")
    private List<WebElement> menuItemsElements;

    @FindBy(jquery = ".rf-ddm-lst:eq(0)")
    private WebElement dropDownMenuPopup;

    @FindBy(jquery = ".rf-ddm-lbl script:last")
    private WebElement script;

    @FindBy(jquery = ".rf-ddm-lbl:eq(0)")
    private WebElement topLvlElement;

    private AdvancedInteractions advancedInteractions;

    @Override
    protected WebElement getMenuPopupInternal() {
        return dropDownMenuPopup;
    }

    @Override
    protected List<WebElement> getMenuItemElementsInternal() {
        return menuItemsElements;
    }

    @Override
    public String getNameOfFragment() {
        return RichFacesDropDownMenu.class.getName();
    }

    @Override
    protected WebElement getScript() {
        return script;
    }

    public AdvancedInteractions advanced() {
        if (advancedInteractions == null) {
            advancedInteractions = new AdvancedInteractions();
        }
        return advancedInteractions;
    }

    public class AdvancedInteractions extends AbstractPopupMenu.AdvancedInteractions {
        public String getLangAttribute() {
            return topLvlElement.getAttribute("lang");
        }

        public WebElement getTopLvlElement() {
            return topLvlElement;
        }
    }
}
