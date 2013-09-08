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
package org.richfaces.tests.metamer.ftest.richPanelMenuItem;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.disabledClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.leftIconClass;
import static org.richfaces.tests.metamer.ftest.BasicAttributes.rightIconClass;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.data;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.disabled;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.leftDisabledIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.leftIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.limitRender;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.mode;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.oncomplete;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.render;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.rendered;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.rightDisabledIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.rightIcon;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.selectable;
import static org.richfaces.tests.metamer.ftest.richPanelMenuItem.PanelMenuItemAttributes.status;
import static org.richfaces.tests.metamer.ftest.webdriver.AttributeList.panelMenuItemAttributes;
import static org.richfaces.tests.page.fragments.impl.panelMenu.PanelMenuHelper.getGuardTypeForMode;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.metamer.ftest.annotations.RegressionTest;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.checker.IconsCheckerWebdriver;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.ui.common.Mode;
import org.richfaces.ui.toggle.panelMenu.PanelMenuMode;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jjamrich@redhat.com">Jan Jamrich</a>
 * @since 4.3.1
 */
public class TestPanelMenuItemSimple extends AbstractWebDriverTest {

    @Page
    PanelMenuItemPage page;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanelMenuItem/simple.xhtml");
    }

    @BeforeMethod
    public void setupMode() {
        panelMenuItemAttributes.set(mode, PanelMenuMode.ajax);
        page.item.setMode(Mode.ajax);
    }

    @Test
    public void testData() {
        String RF = "RichFaces 4";
        panelMenuItemAttributes.set(data, RF);
        panelMenuItemAttributes.set(oncomplete, "data = event.data");

        MetamerPage.requestTimeChangesWaiting(page.item).select();

        expectedReturnJS("return window.data", RF);
    }

    @Test
    public void testDisabled() {
        page.item.setMode(null);
        assertFalse(page.item.isDisabled());

        panelMenuItemAttributes.set(disabled, true);

        assertFalse(page.item.isSelected());
        assertTrue(page.item.isDisabled());

        getGuardTypeForMode(page.item, Mode.client).select();

        assertFalse(page.item.isSelected());
        assertTrue(page.item.isDisabled());
    }

    @Test
    @Templates(value = "plain")
    public void testDisabledClass() {
        panelMenuItemAttributes.set(disabled, true);
        testStyleClass(page.item.getRootElement(), disabledClass);
    }

    @Test
    @Templates(value = "plain")
    public void testLeftDisabledIcon() {
        panelMenuItemAttributes.set(disabled, true);
        verifyStandardIcons(leftDisabledIcon, page.item.getLeftIcon().getRootElement(), page.item.getLeftIcon().getImgIcon(), "");
    }

    @Test
    @Templates(value = "plain")
    public void testLeftIcon() {
        verifyStandardIcons(leftIcon, page.item.getLeftIcon().getRootElement(), page.item.getLeftIcon().getImgIcon(), "");

        panelMenuItemAttributes.set(disabled, true);
        assertTrue(page.item.getLeftIcon().isTransparent());
    }

    @Test
    @Templates(value = "plain")
    public void testLeftIconClass() {
        testStyleClass(page.item.getLeftIcon().getRootElement(), leftIconClass);
    }

    @Test
    public void testLimitRender() {
        panelMenuItemAttributes.set(render, "renderChecker");
        panelMenuItemAttributes.set(limitRender, true);

        String renderChecker = page.getRenderCheckerOutputElement().getText();
        MetamerPage.requestTimeNotChangesWaiting(page.item).select();
        Graphene.waitModel().until("Page was not updated").element(page.getRenderCheckerOutputElement()).text().not().equalTo(renderChecker);
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        assertTrue(page.item.isVisible());

        panelMenuItemAttributes.set(rendered, false);

        assertFalse(page.item.isVisible());
    }

    @Test
    @Templates(value = "plain")
    public void testRightDisabledIcon() {
        panelMenuItemAttributes.set(disabled, true);
        verifyStandardIcons(rightDisabledIcon, page.item.getRightIcon().getRootElement(), page.item.getRightIcon().getImgIcon(), "");
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10519")
    @Templates(value = "plain")
    public void testRightIcon() {
        verifyStandardIcons(rightIcon, page.item.getRightIcon().getRootElement(), page.item.getRightIcon().getImgIcon(), "");

        panelMenuItemAttributes.set(disabled, true);
        assertTrue(page.item.getRightIcon().isTransparent());
    }

    @Test
    @RegressionTest("https://issues.jboss.org/browse/RF-10519")
    @Templates(value = "plain")
    public void testRightIconClass() {
        testStyleClass(page.item.getRightIcon().getRootElement(), rightIconClass);
    }

    @Test
    public void testSelectable() {
        page.item.setMode(null);
        panelMenuItemAttributes.set(selectable, false);
        TimeoutException ex = null;
        try {
            getGuardTypeForMode(page.item, Mode.client).select();
        } catch (TimeoutException e) {
            ex = e;
        }
        if (ex == null) {
            fail("Item should not be selectable when @selectable=false.");
        }
        assertFalse(page.item.isSelected());

        panelMenuItemAttributes.set(selectable, true);
        getGuardTypeForMode(page.item, Mode.ajax).select();
        assertTrue(page.item.isSelected());
    }

    @Test
    public void testStatus() {
        panelMenuItemAttributes.set(status, "statusChecker");

        String statusChecker = page.getStatusCheckerOutputElement().getText();
        page.item.select();
        Graphene.waitAjax().until("Page was not updated").element(page.getStatusCheckerOutputElement()).text().not().equalTo(statusChecker);
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(page.item.getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyleClass(page.item.getRootElement());
    }

    private void verifyStandardIcons(PanelMenuItemAttributes attribute, WebElement icon, WebElement imgIcon, String classSuffix) {
        IconsCheckerWebdriver<PanelMenuItemAttributes> checker = new IconsCheckerWebdriver<PanelMenuItemAttributes>(
                driver, panelMenuItemAttributes, "rf-ico-", "");

        checker.checkCssImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkCssNoImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);
        checker.checkImageIcons(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), imgIcon, classSuffix, false);
        checker.checkNone(attribute, new IconsCheckerWebdriver.WebElementLocator(icon), classSuffix);

    }
}
