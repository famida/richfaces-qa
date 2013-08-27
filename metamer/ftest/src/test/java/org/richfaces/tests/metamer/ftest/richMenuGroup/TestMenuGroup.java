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
package org.richfaces.tests.metamer.ftest.richMenuGroup;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.menuGroupAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.dropDownMenu.RichFacesDropDownMenu;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test case for page /faces/components/richMenuGroup/simple.xhtml
 *
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class TestMenuGroup extends AbstractWebDriverTest {

    @FindBy(jquery = ".rf-tb-itm:eq(0)")
    private RichFacesDropDownMenu fileDropDownMenu;
    @FindBy(jquery = ".rf-ddm-lbl-dec:eq(0)")
    private WebElement target1;
    @FindBy(css = "div[id$=menu1] div.rf-ddm-lbl-dec")
    private WebElement fileMenuLabel;
    @FindBy(css = "div[id$=menu1]")
    private WebElement fileMenu;
    @FindBy(css = "div[id$=menu1_list]")
    private WebElement fileMenuList;
    @FindBy(css = "div[id$=menuGroup4]")
    private WebElement group;
    @FindBy(css = "div[id$=menuGroup4] > span.rf-ddm-itm-lbl ")
    private WebElement label;
    @FindBy(css = "div[id$=menuGroup4_list]")
    private WebElement groupList;
    @FindBy(css = "div[id$=menuGroup4] > span.rf-ddm-itm-ic > img")
    private WebElement icon;
    @FindBy(css = "div[id$=menuGroup4] > span.rf-ddm-itm-ic > span.rf-ddm-emptyIcon")
    private WebElement emptyIcon;
    @FindBy(css = "div[id$=menuItem41]")
    private WebElement menuItem41;
    @Page
    private MetamerPage page;
    @Inject
    @Use(empty = false)
    private Direction direction;
    private int tolerance = 4;

    private enum Direction {

        AUTO("auto"), TOPLEFT("topLeft"), TOPRIGHT("topRight"), BOTTOMLEFT("bottomLeft"), BOTTOMRIGHT("bottomRight"), NULL(
        "null");
        private final String value;

        private Direction(String value) {
            this.value = value;
        }
    }

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richMenuGroup/simple.xhtml");
    }

    private void openMenu() {
        fileDropDownMenu.advanced().invoke(target1);
    }

    private void openMenuAndSubMenu() {
        openMenu();
        new Actions(driver).moveToElement(group).perform();
        Graphene.waitGui().until().element(groupList).is().visible();
    }

    @Test
    public void testDir() {
        testDir(group);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10218")
    @Use(field = "direction", enumeration = true)
    public void testDirection() {
        menuGroupAttributes.set(MenuGroupAttributes.direction, direction.value);
        menuGroupAttributes.set(MenuGroupAttributes.jointPoint, Direction.TOPRIGHT.value);

        Point jointPointLocation, cornerOfPopup = null;

        openMenu();
        jointPointLocation = Utils.getLocations(group).getTopRight();
        openMenuAndSubMenu();
        Locations popupLocations = Utils.getLocations(groupList);

        switch (direction) {
            case TOPLEFT:
                cornerOfPopup = popupLocations.getBottomRight();
                break;
            case TOPRIGHT:
                cornerOfPopup = popupLocations.getBottomLeft();
                break;
            case BOTTOMLEFT:
                cornerOfPopup = popupLocations.getTopRight();
                break;
            case NULL:
            case AUTO:// auto (direction depends on browser/screen resolution)
            case BOTTOMRIGHT:
                cornerOfPopup = popupLocations.getTopLeft();
                break;

        }
        Utils.tolerantAssertPointEquals(cornerOfPopup, jointPointLocation, tolerance, tolerance, "Location of points.");
    }

    @Test
    public void testDisabled() {
        menuGroupAttributes.set(MenuGroupAttributes.disabled, Boolean.TRUE);

        assertTrue(group.getAttribute("class").contains("rf-ddm-itm-dis"), "Menu group should have class \"rf-ddm-itm-dis\".");
        assertPresent(emptyIcon, "Empty icon should be present.");
        assertNotPresent(icon, "Icon should not be present.");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10216")
    public void testHorizontalOffset() {
        int pixels = 100;
        openMenuAndSubMenu();
        Locations locationsBefore = Utils.getLocations(groupList);
        menuGroupAttributes.set(MenuGroupAttributes.horizontalOffset, pixels);
        openMenuAndSubMenu();
        Locations locationsAfter = Utils.getLocations(groupList);
        Utils.tolerantAssertLocationsEquals(locationsBefore.moveAllBy(pixels, 0), locationsAfter, 10, 10, "Horizontal Offset does not work");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9989")
    public void testIcon() {
        menuGroupAttributes.set(MenuGroupAttributes.icon, "null");
        assertNotPresent(icon, "Icon should not be present.");
        assertPresent(emptyIcon, "Empty icon should be present.");

        menuGroupAttributes.set(MenuGroupAttributes.icon, "star");
        assertTrue(icon.getAttribute("src").contains("star.png"),
                "Icon's src attribute should contain \"star.png\".");

        menuGroupAttributes.set(MenuGroupAttributes.icon, "nonexisting");
        assertTrue(icon.getAttribute("src").contains("nonexisting"),
                "Icon's src attribute should contain \"nonexisting\".");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-9989")
    public void testIconDisabled() {
        menuGroupAttributes.set(MenuGroupAttributes.disabled, Boolean.TRUE);

        assertNotPresent(icon, "Icon should not be present.");
        assertPresent(emptyIcon, "Empty icon should be present.");

        menuGroupAttributes.set(MenuGroupAttributes.iconDisabled, "star");
        assertTrue(icon.getAttribute("src").contains("star.png"),
                "Icon's src attribute should contain \"star.png\".");

        menuGroupAttributes.set(MenuGroupAttributes.iconDisabled, "nonexisting");
        assertTrue(icon.getAttribute("src").contains("nonexisting"),
                "Icon's src attribute should contain \"nonexisting\".");
    }

    @Test
    public void testInit() {
        assertPresent(fileMenu, "Drop down menu \"File\" should be present on the page");
        assertVisible(fileMenu, "Drop down menu \"File\" should be visible on the page");

        assertPresent(group, "Menu group \"Save As...\" should be present on the page");
        assertNotVisible(group, "Menu group \"Save As...\" should not be visible on the page");

        assertNotVisible(fileMenuList, "Menu should not be expanded.");
        Graphene.guardNoRequest(fileDropDownMenu).advanced().invoke(target1);
        assertVisible(fileMenuList, "Menu should be expanded.");

        assertPresent(group, "M enu group \"Save As...\" should be present on the page");
        assertVisible(group, "Menu group \"Save As...\" should be visible on the page");

        assertPresent(menuItem41, "Menu item \"Save\" should be present on the page");
        assertNotVisible(menuItem41, "Menu item \"Save\" should not be visible on the page");

        assertNotVisible(groupList, "Submenu should not be expanded.");
        Graphene.guardNoRequest(new Actions(driver).moveToElement(group).build()).perform();
        assertVisible(groupList, "Submenu should be expanded.");

        assertPresent(menuItem41, "Menu item \"Save\" should be present on the page");
        assertVisible(menuItem41, "Menu item \"Save\" should be visible on the page");

        assertNotPresent(icon, "Icon of menu group should not be present on the page");

        assertPresent(fileMenuLabel, "Label of menu should be present on the page");
        assertVisible(fileMenuLabel, "Label of menu should be visible on the page");
        assertEquals(fileMenuLabel.getText(), "File", "Label of the menu");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10218")
    @Use(field = "direction", enumeration = true)
    public void testJointPoint() {
        menuGroupAttributes.set(MenuGroupAttributes.jointPoint, direction.value);
        menuGroupAttributes.set(MenuGroupAttributes.direction, Direction.BOTTOMRIGHT.value);
        Point jointPointLocation = null, cornerOfPopup = null;
        openMenu();
        Locations jointPointLocations = Utils.getLocations(group);
        switch (direction) {
            case BOTTOMLEFT:
                jointPointLocation = jointPointLocations.getBottomLeft();
                break;
            case BOTTOMRIGHT:
                jointPointLocation = jointPointLocations.getBottomRight();
                break;
            case TOPLEFT:
                jointPointLocation = jointPointLocations.getTopLeft();
                break;
            case NULL:
            case AUTO:
            // auto (direction depends on browser/screen resolution)
            case TOPRIGHT:
                jointPointLocation = jointPointLocations.getTopRight();
                break;
        }
        openMenuAndSubMenu();
        cornerOfPopup = Utils.getLocations(groupList).getTopLeft();
        Utils.tolerantAssertPointEquals(jointPointLocation, cornerOfPopup, tolerance, tolerance, "Joint Point");
    }

    @Test
    public void testLabel() {
        String testedLabel = "new label";
        menuGroupAttributes.set(MenuGroupAttributes.label, testedLabel);
        openMenuAndSubMenu();
        assertEquals(label.getText(), testedLabel, "New label of the menu group.");
    }

    @Test
    public void testLang() {
        testAttributeLang(group);
    }

    @Test
    public void testOnclick() {
        testFireEvent(Event.CLICK, group);
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, group);
    }

    @Test
    public void testOnhide() {
        testFireEvent("hide", new Action() {
            @Override
            public void perform() {
                openMenuAndSubMenu();
                new Actions(driver).moveToElement(page.getRequestTimeElement()).perform();
                Graphene.waitGui().until().element(groupList).is().not().visible();
            }
        });
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, group);
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, group);
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, group);
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, group);
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, group);
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, group);
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, group);
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, group);
    }

    @Test
    public void testOnshow() {
        testFireEvent("show", new Action() {
            @Override
            public void perform() {
                openMenuAndSubMenu();
            }
        });
    }

    @Test
    public void testRendered() {
        menuGroupAttributes.set(MenuGroupAttributes.rendered, false);
        assertNotPresent(group, "Menu group should not be rendered when rendered=false.");
    }

    @Test
    public void testStyle() {
        testStyle(group);
    }

    @Test
    public void testStyleClass() {
        testStyleClass(group);
    }

    @Test
    public void testTitle() {
        testTitle(group);
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10216")
    public void testVerticalOffset() {
        int pixels = 100;
        openMenuAndSubMenu();
        Locations locationsBefore = Utils.getLocations(groupList);
        menuGroupAttributes.set(MenuGroupAttributes.verticalOffset, pixels);
        openMenuAndSubMenu();
        Locations locationsAfter = Utils.getLocations(groupList);
        Utils.tolerantAssertLocationsEquals(locationsBefore.moveAllBy(0, pixels), locationsAfter, 10, 10, "Vertical Offset does not work");
    }

    @BeforeMethod
    private void updateDropDownMenuInvoker() {
        fileDropDownMenu.advanced().setInvoker(fileDropDownMenu.advanced().HOVER_INVOKER);
    }
}
