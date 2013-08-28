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
package org.richfaces.tests.metamer.ftest.richOrderingList;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.net.URL;

import javax.faces.event.PhaseId;

import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.richfaces.tests.metamer.ftest.BasicAttributes;
import org.richfaces.tests.metamer.ftest.annotations.Templates;
import org.richfaces.tests.metamer.ftest.webdriver.MetamerPage;
import org.richfaces.tests.page.fragments.impl.Utils;
import org.richfaces.tests.page.fragments.impl.utils.Event;
import org.richfaces.tests.page.fragments.impl.utils.picker.ChoicePickerHelper;
import org.testng.annotations.Test;

/**
 * Selenium tests for page faces/components/richOrderingList/withColumn.xhtml.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestOrderingListAttributes extends AbstractOrderingListTest {

    @Page
    private MetamerPage page;

    @ArquillianResource
    private JavascriptExecutor executor;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richOrderingList/withColumn.xhtml");
    }

    @Test
    public void testCaption() {
        String testedValue = "New Caption";
        attributes.set(OrderingListAttributes.caption, testedValue);
        assertEquals(orderingList.advanced().getCaptionElement().getText(), testedValue);
    }

    @Test
    @Templates(value = "plain")
    public void testColumnClasses() {
        String testedClass = "metamer-ftest-class";
        attributes.set(OrderingListAttributes.columnClasses, testedClass);
        for (WebElement li : orderingList.advanced().getItems()) {
            for (WebElement e : li.findElements(By.tagName("td"))) {
                assertTrue(e.getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
            }
        }
    }

    @Test
    public void testDisabled() {
        attributes.set(OrderingListAttributes.disabled, Boolean.TRUE);
        for (WebElement li : orderingList.advanced().getItems()) {
            assertTrue(li.getAttribute("class").contains("rf-ord-opt-dis"), "Item @class should contain " + "rf-ord-opt-dis");
        }
        try {
            orderingList.select(0);
            fail("The attribute <disabled> is set to true, but the ordering list is still enabled.");
        } catch (TimeoutException e) {
        }
    }

    @Test
    @Templates(value = "plain")
    public void testDisabledClass() {
        attributes.set(OrderingListAttributes.disabled, Boolean.TRUE);
        testStyleClass(orderingList.advanced().getRootElement(), BasicAttributes.disabledClass);
    }

    @Test
    @Templates(value = "plain")
    public void testDownBottomText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.downBottomText, testedValue);
        assertEquals(orderingList.advanced().getBottomButtonElement().getText(), testedValue);
    }

    @Test
    @Templates(value = "plain")
    public void testDownText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.downText, testedValue);
        assertEquals(orderingList.advanced().getDownButtonElement().getText(), testedValue);
    }

    @Test
    @Templates(value = "plain")
    public void testHeaderClass() {
        testStyleClass(orderingList.advanced().getHeaderElement(), BasicAttributes.headerClass);
    }

    @Test
    public void testImmediate() {
        attributes.set(OrderingListAttributes.immediate, Boolean.FALSE);
        orderingList.select(1).putItBefore(0);
        submit();
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed");

        attributes.set(OrderingListAttributes.immediate, Boolean.TRUE);
        orderingList.select(1).putItBefore(0);
        submit();
        page.assertPhases(PhaseId.ANY_PHASE);
        page.assertListener(PhaseId.APPLY_REQUEST_VALUES, "value changed");
    }

    @Test
    @Templates(value = "plain")
    public void testItemClass() {
        String testedClass = "metamer-ftest-class";
        attributes.set(OrderingListAttributes.itemClass, "metamer-ftest-class");
        for (WebElement element : orderingList.advanced().getItems()) {
            assertTrue(element.getAttribute("class").contains(testedClass), "Item @class should contain " + testedClass);
        }
    }

    @Test
    public void testListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        attributes.set(OrderingListAttributes.listHeight, testedValue);
        assertEquals(Integer.valueOf(orderingList.advanced().getListAreaElement().getCssValue("height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testListWidth() {
        int testedValue = 600;
        int tolerance = 10;
        attributes.set(OrderingListAttributes.listWidth, testedValue);
        assertEquals(Integer.valueOf(orderingList.advanced().getListAreaElement().getCssValue("width").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testMaxListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        attributes.set(OrderingListAttributes.maxListHeight, testedValue);
        attributes.set(OrderingListAttributes.listHeight, "");
        assertEquals(Integer.valueOf(orderingList.advanced().getListAreaElement().getCssValue("max-height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testMinListHeight() {
        int testedValue = 600;
        int tolerance = 10;
        attributes.set(OrderingListAttributes.listHeight, "");
        attributes.set(OrderingListAttributes.minListHeight, testedValue);
        assertEquals(Integer.valueOf(orderingList.advanced().getListAreaElement().getCssValue("min-height").replace("px", "")), testedValue, tolerance);
    }

    @Test
    public void testOnblur() {
        testFireEvent(attributes, OrderingListAttributes.onblur, new Action() {
            @Override
            public void perform() {
                //have to be this way or the blur will not be triggered
                orderingList.advanced().getListAreaElement().click();
                orderingList.advanced().getRootElement().click();
                waiting(500);
                Utils.triggerJQ(executor, "blur", orderingList.advanced().getItems().get(0));
            }
        });
    }

    @Test
    public void testOnchange() {
        testFireEvent(Event.CHANGE, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnclick() {
        testFireEvent(attributes, OrderingListAttributes.onlistclick,
            new Actions(driver).click(orderingList.advanced().getListAreaElement()).build());
    }

    @Test
    public void testOndblclick() {
        testFireEvent(Event.DBLCLICK, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnfocus() {
        testFireEvent(attributes, OrderingListAttributes.onfocus,
            new Actions(driver).click(orderingList.advanced().getItems().get(0)).build());
    }

    @Test
    public void testOnkeydown() {
        testFireEvent(Event.KEYDOWN, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnkeypress() {
        testFireEvent(Event.KEYPRESS, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnkeyup() {
        testFireEvent(Event.KEYUP, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnlistclick() {
        testFireEvent(attributes, OrderingListAttributes.onlistclick, new Actions(driver).click(orderingList.advanced().getItems().get(0)).build());
    }

    @Test
    public void testOnlistdblclick() {
        testFireEvent(Event.DBLCLICK, orderingList.advanced().getItems().get(0), "listdblclick");
    }

    @Test
    public void testOnlistkeydown() {
        testFireEvent(Event.KEYDOWN, orderingList.advanced().getItems().get(0), "listkeydown");
    }

    @Test
    public void testOnlistkeypress() {
        testFireEvent(Event.KEYPRESS, orderingList.advanced().getItems().get(0), "listkeypress");
    }

    @Test
    public void testOnlistkeyup() {
        testFireEvent(Event.KEYUP, orderingList.advanced().getItems().get(0), "listkeyup");
    }

    @Test
    public void testOnlistmousedown() {
        testFireEvent(Event.MOUSEDOWN, orderingList.advanced().getItems().get(0), "listmousedown");
    }

    @Test
    public void testOnlistmousemove() {
        testFireEvent(Event.MOUSEMOVE, orderingList.advanced().getItems().get(0), "listmousemove");
    }

    @Test
    public void testOnlistmouseout() {
        testFireEvent(Event.MOUSEOUT, orderingList.advanced().getItems().get(0), "listmouseout");
    }

    @Test
    public void testOnlistmouseover() {
        testFireEvent(Event.MOUSEOVER, orderingList.advanced().getItems().get(0), "listmouseover");
    }

    @Test
    public void testOnlistmouseup() {
        testFireEvent(Event.MOUSEUP, orderingList.advanced().getItems().get(0), "listmouseup");
    }

    @Test
    public void testOnmousedown() {
        testFireEvent(Event.MOUSEDOWN, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnmousemove() {
        testFireEvent(Event.MOUSEMOVE, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnmouseout() {
        testFireEvent(Event.MOUSEOUT, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnmouseover() {
        testFireEvent(Event.MOUSEOVER, orderingList.advanced().getListAreaElement());
    }

    @Test
    public void testOnmouseup() {
        testFireEvent(Event.MOUSEUP, orderingList.advanced().getListAreaElement());
    }

    @Test
    @Templates(value = "plain")
    public void testRendered() {
        attributes.set(OrderingListAttributes.rendered, false);
        assertNotPresent(orderingList.advanced().getRootElement(), "The attribute <rendered> is set to <false>, but it has no effect.");
    }

    @Test
    @Templates(value = "plain")
    public void testSelectItemClass() {
        attributes.set(OrderingListAttributes.selectItemClass, "metamer-ftest-class");
        orderingList.select(0);
        assertTrue(orderingList.advanced().getSelectedItems().get(0).getAttribute("class").contains("metamer-ftest-class"), "The attribute <selectItemClass> is set to <metamer-ftest-class>, but it has no effect.");
    }

    @Test
    @Templates(value = "plain")
    public void testStyle() {
        testStyle(orderingList.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testStyleClass() {
        testStyle(orderingList.advanced().getRootElement());
    }

    @Test
    @Templates(value = "plain")
    public void testUpText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.upText, testedValue);
        assertEquals(orderingList.advanced().getUpButtonElement().getText(), testedValue);
    }

    @Test
    @Templates(value = "plain")
    public void testUpTopText() {
        String testedValue = "New text";
        attributes.set(OrderingListAttributes.upTopText, testedValue);
        assertEquals(orderingList.advanced().getTopButtonElement().getText(), testedValue);
    }

    @Test
    public void testValueChangeListener() {
        orderingList.select(0).putItAfter(ChoicePickerHelper.byIndex().last());
        submit();
        page.assertListener(PhaseId.PROCESS_VALIDATIONS, "value changed");
    }
}