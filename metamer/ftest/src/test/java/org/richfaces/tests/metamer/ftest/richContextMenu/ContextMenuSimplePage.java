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
package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.contextMenuAttributes;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest.DriverType;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.metamer.ftest.webdriver.utils.StopWatch;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.contextMenu.RichFacesContextMenu;
import org.richfaces.ui.common.Positioning;

/**
 * Page object rich:contextMenu component at faces/components/richContextMenu/simple.xhtml
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @since 4.2.1.Final
 */
public class ContextMenuSimplePage extends MetamerPage {

    @FindBy(css = "div[id$=targetPanel1]")
    private WebElement targetPanel1;

    @FindBy(css = "div[id$=targetPanel2]")
    private WebElement targetPanel2;

    @FindBy(css = "div.rf-ctx-lst")
    private WebElement contextMenuContent;

    @FindBy(css = "div[id$=ctxMenu]")
    private RichFacesContextMenu contextMenu;

    @FindBy(jquery = "span[id$=output]")
    private WebElement output;

    @FindBy(jquery = "div[id$=\":ctxMenu\"]")
    private WebElement contextMenuRoot;

    @FindBy(tagName = "body")
    private WebElement body;

    @FindBy(jquery = "div[id$=menuGroup4_list]")
    private WebElement groupList;

    public final int SHOW_DELAY_TOLERANCE = 600;

    public void clickOnFirstPanel(DriverType type) {
        if (type == DriverType.InternetExplorer) {
            targetPanel1.sendKeys(Keys.CONTROL);
        }
        targetPanel1.click();
    }

    public void clickOnSecondPanel(DriverType type) {
        if (type == DriverType.InternetExplorer) {
            targetPanel2.sendKeys(Keys.CONTROL);
        }
        targetPanel2.click();
    }

    public void waitUntilContextMenuAppears() {
        Graphene.waitModel().withMessage("Context menu did not show.").until().element(contextMenuContent).is().visible();
    }

    public void waitUntilContextMenuHides() {
        Graphene.waitModel().withMessage("Context menu should not be visible.")
                .until().element(contextMenuContent).is().not().visible();
    }

    public Locations getContextMenuLocations() {
        contextMenu.advanced().invoke(targetPanel2);
        Locations contextMenuLocations = Utils.getLocations(contextMenuContent);
        contextMenu.advanced().dismiss();
        return contextMenuLocations;
    }

    public Locations getContextMenuLocationsWhenPosition(Positioning positioning) {
        contextMenuAttributes.set(ContextMenuAttributes.direction, positioning);
        contextMenu.advanced().invoke(targetPanel2);
        Locations contextMenuLocations = Utils.getLocations(contextMenuContent);
        contextMenu.advanced().dismiss();
        return contextMenuLocations;
    }

    public int getActualShowDelay(final int showDelay) {
    contextMenuAttributes.set(ContextMenuAttributes.showDelay, showDelay);
        return StopWatch.watchTimeSpentInAction(new StopWatch.PerformableAction() {
            @Override
            public void perform() {
                targetPanel1.click();
                contextMenu.advanced().waitUntilIsVisible();
            }
        }).inMillis().intValue();
    }

    public void checkShowDelay(int expected) {
        assertEquals(getActualShowDelay(expected), expected, SHOW_DELAY_TOLERANCE);
    }

    public static String trimTheRGBAColor(String original) {
        return original.replaceAll("\\s", "");
    }

    public WebElement getTargetPanel1() {
        return targetPanel1;
    }

    public void setTargetPanel1(WebElement targetPanel1) {
        this.targetPanel1 = targetPanel1;
    }

    public WebElement getTargetPanel2() {
        return targetPanel2;
    }

    public void setTargetPanel2(WebElement targetPanel2) {
        this.targetPanel2 = targetPanel2;
    }

    public WebElement getContextMenuContent() {
        return contextMenuContent;
    }

    public void setContextMenuContent(WebElement contextMenuContent) {
        this.contextMenuContent = contextMenuContent;
    }

    public RichFacesContextMenu getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(RichFacesContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    public WebElement getOutput() {
        return output;
    }

    public void setOutput(WebElement output) {
        this.output = output;
    }

    public WebElement getContextMenuRoot() {
        return contextMenuRoot;
    }

    public void setContextMenuRoot(WebElement contextMenuRoot) {
        this.contextMenuRoot = contextMenuRoot;
    }

    public WebElement getBody() {
        return body;
    }

    public void setBody(WebElement body) {
        this.body = body;
    }

    public WebElement getGroupList() {
        return groupList;
    }

    public void setGroupList(WebElement groupList) {
        this.groupList = groupList;
    }
}
