/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.richfaces.tests.page.fragments.impl.calendar.common;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.richfaces.tests.page.fragments.impl.calendar.common.dayPicker.RichFacesDayPicker;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.RichFacesCalendarEditor;
import org.richfaces.tests.page.fragments.impl.calendar.common.editor.time.TimeEditor;

/**
 * Component for footer controls of calendar.
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class RichFacesFooterControls implements FooterControls {

    @Root
    protected WebElement root;

    @Drone
    protected WebDriver driver;
    //
    protected RichFacesCalendarEditor calendarEditor;
    protected RichFacesDayPicker dayPicker;
    //
    @FindByJQuery("div.rf-cal-tl-btn:contains('Clean')")
    protected WebElement cleanButtonElement;
    @FindBy(css = "td.rf-cal-tl-ftr > div[onclick*='showTimeEditor']")
    protected WebElement timeEditorOpenerElement;
    @FindBy(css = "td.rf-cal-tl-ftr > div[onclick*='showSelectedDate']")
    protected WebElement selectedDateElement;
    @FindByJQuery("div.rf-cal-tl-btn:contains('Today')")
    protected WebElement todayButtonElement;

    private void _openTimeEditor() {
        if (!isVisible()) {
            throw new RuntimeException("Cannot open time editor. "
                    + "Ensure that calendar popup and footer controls are displayed.");
        }
        if (new WebElementConditionFactory(timeEditorOpenerElement).not().isVisible().apply(driver)) {
            throw new RuntimeException("Cannot open time editor. "
                    + "Ensure that the date is set before setting time.");
        }
        timeEditorOpenerElement.click();
        Graphene.waitGui().until(calendarEditor.getTimeEditor().isVisibleCondition());
    }

    @Override
    public void cleanDate() {
        if (!isVisible()) {
            throw new RuntimeException("Footer controls are not displayed, cannot interact with  clean button");
        }
        if (new WebElementConditionFactory(cleanButtonElement).not().isVisible().apply(driver)) {
            throw new RuntimeException("Clean button is not displayed.");
        }
        cleanButtonElement.click();
        Graphene.waitGui().until(new WebElementConditionFactory(cleanButtonElement).not().isVisible());
    }

    @Override
    public WebElement getCleanButtonElement() {
        return cleanButtonElement;
    }

    @Override
    public TimeEditor getTimeEditor() {
        return calendarEditor.getTimeEditor();
    }

    @Override
    public WebElement getTimeEditorOpenerElement() {
        return timeEditorOpenerElement;
    }

    @Override
    public WebElement getTodayButtonElement() {
        return todayButtonElement;
    }

    @Override
    public WebElement getSelectedDateElement() {
        return selectedDateElement;
    }

    @Override
    public ExpectedCondition<Boolean> isNotVisibleCondition() {
        return new WebElementConditionFactory(root).not().isVisible();
    }

    @Override
    public boolean isVisible() {
        return isVisibleCondition().apply(driver);
    }

    @Override
    public ExpectedCondition<Boolean> isVisibleCondition() {
        return new WebElementConditionFactory(root).isVisible();
    }

    @Override
    public TimeEditor openTimeEditor() {
        if (calendarEditor.getTimeEditor().isVisible()) {
            return calendarEditor.getTimeEditor();
        } else {
            _openTimeEditor();
            return calendarEditor.getTimeEditor();
        }
    }

    public void setCalendarEditor(RichFacesCalendarEditor calendarEditor) {
        this.calendarEditor = calendarEditor;
    }

    public void setDayPicker(RichFacesDayPicker dayPicker) {
        this.dayPicker = dayPicker;
    }

    @Override
    public void setTodaysDate() {
        todayDate();
    }

    @Override
    public void todayDate() {
        if (!isVisible()) {
            throw new RuntimeException("Footer controls are not displayed, cannot interact with today button");
        }
        if (new WebElementConditionFactory(todayButtonElement).not().isVisible().apply(driver)) {
            throw new RuntimeException("Today button is not displayed.");
        }
        todayButtonElement.click();
    }
}
