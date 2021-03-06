/**
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
 */
package org.richfaces.tests.metamer.ftest.richContextMenu;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.contextMenuAttributes;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.Inject;
import org.richfaces.tests.metamer.ftest.annotations.IssueTracking;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.annotations.Use;
import org.richfaces.tests.page.fragments.impl.Locations;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.richfaces.ui.common.Positioning;
import org.testng.annotations.Test;

/**
 * Test for rich:contextMenu component at faces/components/richContextMenu/simple.xhtml
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @since 4.2.1.Final
 */
public class TestContextMenu extends AbstractWebDriverTest {

    @Page
    private ContextMenuSimplePage page;
    @Inject
    @Use(empty = false)
    private Integer delay;
    @Inject
    @Use(empty = false)
    private Positioning positioning;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richContextMenu/simple.xhtml");
    }

    private void updateShowAction() {
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
        page.getContextMenu().advanced().setupShowEvent(Event.CLICK);
    }

    @Test
    @Use(field = "delay", ints = { 1500, 2000, 2500 })
    public void testHideDelay() {
        updateShowAction();
        testDelay(new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                page.waitUntilContextMenuAppears();
            }
        }, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().setupHideWaitUntilIsNotVisibleTimeout(3);
                page.getContextMenu().advanced().hide();
            }
        }, "hideDelay", delay);
    }

    @Test
    public void testOnhide() {
        updateShowAction();
        final String VALUE = "hide";
        // set onhide
        contextMenuAttributes.set(ContextMenuAttributes.onhide, "metamerEvents += \"" + VALUE + "\"");
        // show context menu
        page.clickOnFirstPanel(driverType);
        // check whether the context menu is displayed
        page.waitUntilContextMenuAppears();
        // lose focus >>> menu will disappear
        page.clickOnSecondPanel(driverType);
        // check whether the context menu isn't displayed
        page.waitUntilContextMenuHides();
        assertEquals(expectedReturnJS("return metamerEvents", VALUE), VALUE);
    }

    @Test
    public void testVerticalOffset() {
        updateShowAction();
        int offset = 11;
        // show context menu
        page.clickOnFirstPanel(driverType);
        // check whether the context menu is displayed
        page.waitUntilContextMenuAppears();
        // get position before offset is set
        Point before = page.getContextMenuContent().getLocation();
        // set verticalOffset
        contextMenuAttributes.set(ContextMenuAttributes.verticalOffset, offset);
        // show context menu
        page.clickOnFirstPanel(driverType);
        // check whether the context menu is displayed
        page.waitUntilContextMenuAppears();
        // get position after offset is set
        Point after = page.getContextMenuContent().getLocation();
        // check offset
        assertEquals(before.getY(), after.getY() - offset);
    }

    @Test
    @Templates("plain")
    public void testDir() {
        updateShowAction();
        String expected = "rtl";
        contextMenuAttributes.set(ContextMenuAttributes.dir, expected);

        page.getContextMenu().advanced().show(page.getTargetPanel1());
        String directionCSS = page.getContextMenu().advanced().getItemsElements().get(0).getCssValue("direction");
        assertEquals(directionCSS, expected, "The direction attribute was not applied correctly!");
    }

    @Test
    @Use(field = "positioning", enumeration = true)
    public void testDirection() {
        driver.manage().window().setSize(new Dimension(1280, 1024));// for stabilizing job in all templates
        int tolerance = 10;// px
        String msg = "The actual menu locations should be same as shifted default locations.";
        // setting up the right panel because then the context menu will fit on the page
        contextMenuAttributes.set(ContextMenuAttributes.target, "targetPanel2");
        contextMenuAttributes.set(ContextMenuAttributes.direction, "bottomRight");

        Locations defaultLocations = page.getContextMenuLocations();// bottom right
        Locations actMenuLocation = page.getContextMenuLocationsWhenPosition(positioning);

        int defaultWidth = defaultLocations.getWidth();
        int defaultHeight = defaultLocations.getHeight();
        int shiftX = 0;
        int shiftY = 0;

        assertEquals(actMenuLocation.getHeight(), defaultHeight, tolerance, "Height of context menu should be same as before.");
        assertEquals(actMenuLocation.getWidth(), defaultWidth, tolerance, "Width of context menu should be same as before.");
        switch (positioning) {
            case auto:
            case bottomRight:
            case autoRight:
            case bottomAuto:
                // no shifting
                break;
            case autoLeft:
            case bottomLeft:
                shiftX = -defaultWidth;
                break;
            case topAuto:
            case topRight:
                shiftY = -defaultHeight;
                break;
            case topLeft:
                shiftX = -defaultWidth;
                shiftY = -defaultHeight;
                break;
            default:
                throw new IllegalArgumentException("Uknown switch " + positioning);
        }
        // the actual menu locations should be same as shifted default locations
        Utils.tolerantAssertLocationsEquals(defaultLocations.moveAllBy(shiftX, shiftY), actMenuLocation, tolerance, tolerance,
            msg);
    }

    @Test
    @Templates("plain")
    public void testLang() {
        updateShowAction();
        String langVal = "cs";
        contextMenuAttributes.set(ContextMenuAttributes.lang, langVal);
        page.getContextMenu().advanced().show(page.getTargetPanel1());

        assertEquals(page.getContextMenu().advanced().getLangAttribute(), langVal, "The lang attribute was not set correctly!");
    }

    @Test
    public void testMode() {
        updateShowAction();
        // ajax
        contextMenuAttributes.set(ContextMenuAttributes.mode, "ajax");
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        guardAjax(page.getContextMenu().advanced().getItemsElements().get(0)).click();
        assertEquals(page.getOutput().getText(), "Open", "Menu action was not performed.");

        // server
        contextMenuAttributes.set(ContextMenuAttributes.mode, "server");
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        guardHttp(page.getContextMenu().advanced().getItemsElements().get(8)).click();
        assertEquals(page.getOutput().getText(), "Exit", "Menu action was not performed.");

        // client
        contextMenuAttributes.set(ContextMenuAttributes.mode, "client");
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        guardNoRequest(page.getContextMenu().advanced().getItemsElements().get(0)).click();
    }

    @Test
    public void testDisabled() {
        updateShowAction();
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        assertTrue(page.getContextMenuContent().isDisplayed());

        contextMenuAttributes.set(ContextMenuAttributes.disabled, true);

        try {
            page.getContextMenu().advanced().show(page.getTargetPanel1());
            fail("The context menu should not be showd when disabled!");
        } catch (TimeoutException ex) {
            // OK
        }
    }

    @Test
    public void testHorizontalOffset() {
        updateShowAction();
        int offset = 11;

        page.getContextMenu().advanced().show(page.getTargetPanel1());
        Point positionBefore = page.getContextMenuContent().getLocation();

        contextMenuAttributes.set(ContextMenuAttributes.horizontalOffset, offset);
        page.getContextMenu().advanced().show(page.getTargetPanel1());

        Point positionAfter = page.getContextMenuContent().getLocation();

        assertEquals(positionAfter.getX(), positionBefore.getX() + offset);
    }

    @Test
    @Templates("plain")
    public void testStyle() {
        updateShowAction();
        String color = "yellow";
        String styleVal = "background-color: " + color + ";";
        contextMenuAttributes.set(ContextMenuAttributes.style, styleVal);
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        String backgroundColor = page.getContextMenuRoot().getCssValue("background-color");
        // webdriver retrieves the color in rgba format
        assertEquals(ContextMenuSimplePage.trimTheRGBAColor(backgroundColor), "rgba(255,255,0,1)",
            "The style was not applied correctly!");
    }

    @Test
    @Templates("plain")
    public void testStyleClass() {
        updateShowAction();
        String styleClassVal = "test-style-class";
        contextMenuAttributes.set(ContextMenuAttributes.styleClass, styleClassVal);
        String styleClass = page.getContextMenuRoot().getAttribute("class");
        assertTrue(styleClass.contains(styleClassVal));
    }

    @Test
    public void testTarget() {
        updateShowAction();
        // contextMenu element is present always. Check if is displayed
        contextMenuAttributes.set(ContextMenuAttributes.target, "targetPanel2");
        assertFalse(page.getContextMenuContent().isDisplayed());
        page.getContextMenu().advanced().show(page.getTargetPanel2());

        contextMenuAttributes.set(ContextMenuAttributes.target, "targetPanel1");
        assertFalse(page.getContextMenuContent().isDisplayed());
        page.getContextMenu().advanced().show(page.getTargetPanel1());
    }

    @Test
    @Templates("plain")
    public void testTitle() {
        updateShowAction();
        String titleVal = "test title";
        contextMenuAttributes.set(ContextMenuAttributes.title, titleVal);
        assertEquals(page.getContextMenuRoot().getAttribute("title"), titleVal);
    }

    @Test
    @Templates("plain")
    public void testRendered() {
        updateShowAction();
        contextMenuAttributes.set(ContextMenuAttributes.rendered, Boolean.FALSE);
        try {
            page.getContextMenu().advanced().show(page.getTargetPanel1());
            fail("The context menu should not be invoked when rendered == false!");
        } catch (TimeoutException ex) {
            // OK
        }
    }

    @Test
    public void testPopupWidth() {
        updateShowAction();
        String minWidth = "333";
        contextMenuAttributes.set(ContextMenuAttributes.popupWidth, minWidth);
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        String style = page.getContextMenuContent().getCssValue("min-width");
        assertEquals(style, minWidth + "px");
    }

    @Test
    public void testOnshow() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onshow, new Actions(driver).click(page.getTargetPanel1())
            .build());
    }

    @Test
    @Templates("plain")
    public void testOnclick() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onclick, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                page.getContextMenu().advanced().getItemsElements().get(1).click();
            }
        });
    }

    @Test
    @Templates("plain")
    public void testOndblclick() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.ondblclick, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).doubleClick(page.getContextMenu().advanced().getItemsElements().get(1)).build().perform();
            }
        });
    }

    @Test
    public void testOnitemclick() {
        updateShowAction();
        testOnclick();
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12792")
    public void testOnkeydown() {
        updateShowAction();
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        testFireEventWithJS(page.getContextMenu().advanced().getItemsElements().get(1), Event.KEYDOWN, contextMenuAttributes,
            ContextMenuAttributes.onkeydown);
    }

    @Test
    public void testOnkeyup() {
        updateShowAction();
        page.getContextMenu().advanced().show(page.getTargetPanel1());
        testFireEventWithJS(page.getContextMenu().advanced().getItemsElements().get(1), Event.KEYUP, contextMenuAttributes,
            ContextMenuAttributes.onkeyup);
    }

    @Test
    @Templates("plain")
    public void testOnkeypress() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onkeypress, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).sendKeys("a").build().perform();
            }
        });
    }

    @Test
    public void testOnmousedown() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmousedown, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                mouse.mouseDown(((Locatable) page.getContextMenu().advanced().getItemsElements().get(1)).getCoordinates());
            }
        });
        // release mouse button - necessary since Selenium 2.35
        new Actions(driver).release().perform();
    }

    @Test
    public void testOnmouseup() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmouseup, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                Mouse mouse = ((HasInputDevices) driver).getMouse();
                Coordinates coords = ((Locatable) page.getContextMenu().advanced().getItemsElements().get(1)).getCoordinates();
                mouse.mouseDown(coords);
                mouse.mouseUp(coords);
            }
        });
    }

    @Test
    @Templates("plain")
    public void testOnmousemove() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmousemove, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItemsElements().get(1)).build().perform();
            }
        });
    }

    @Test(groups = "Future")
    @IssueTracking("https://issues.jboss.org/browse/RF-12854")
    public void testOnmouseout() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.onmouseout, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItemsElements().get(2)).build().perform();
                waitModel().until().element(page.getGroupList()).is().visible();
                new Actions(driver).moveToElement(page.getRequestTimeElement()).build().perform();
                waitGui().until().element(page.getGroupList()).is().not().visible();
            }
        });
    }

    @Test
    public void testOnmouseover() {
        updateShowAction();
        testOnmousemove();
    }

    @Test
    public void testOngroupshow() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.ongroupshow, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItemsElements().get(2)).build().perform();
                waitGui().until().element(page.getGroupList()).is().visible();
            }
        });
    }

    @Test
    public void testOngrouphide() {
        updateShowAction();
        testFireEvent(contextMenuAttributes, ContextMenuAttributes.ongrouphide, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().show(page.getTargetPanel1());
                new Actions(driver).moveToElement(page.getContextMenu().advanced().getItemsElements().get(2)).build().perform();
                waitGui().until().element(page.getGroupList()).is().visible();
                new Actions(driver).click(page.getContextMenu().advanced().getItemsElements().get(1)).build().perform();
                waitGui().until().element(page.getGroupList()).is().not().visible();
            }
        });
    }

    @Test
    @Use(field = "delay", ints = { 1500, 2000, 2500 })
    public void testShowDelay() {
        updateShowAction();
        testDelay(new Action() {
            @Override
            public void perform() {
                try {
                    page.getContextMenu().advanced().hide();
                } catch (IllegalStateException ignored) {
                }
                page.waitUntilContextMenuHides();
            }
        }, new Action() {
            @Override
            public void perform() {
                page.getContextMenu().advanced().setupShowWaitUntilIsVisibleTimeout(3);
                page.getContextMenu().advanced().show(page.getTargetPanel1());
            }
        }, "showDelay", delay);
    }

    @Test
    public void testShowEvent() {
        updateShowAction();
        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "hover");
        page.getContextMenu().advanced().setupShowEvent(Event.MOUSEOVER);
        page.getContextMenu().advanced().show(page.getTargetPanel1());

        contextMenuAttributes.set(ContextMenuAttributes.showEvent, "click");
        page.getContextMenu().advanced().setupShowEvent(Event.CLICK);
        page.getContextMenu().advanced().show(page.getTargetPanel1());
    }

    @Test
    public void testTargetSelector() {
        updateShowAction();
        contextMenuAttributes.set(ContextMenuAttributes.targetSelector, "div[id*=targetPanel]");

        page.getContextMenu().advanced().show(page.getTargetPanel1());
        page.getContextMenu().advanced().hide();
    }
}
