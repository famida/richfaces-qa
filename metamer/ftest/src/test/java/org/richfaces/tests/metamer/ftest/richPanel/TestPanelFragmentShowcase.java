package org.richfaces.tests.metamer.ftest.richPanel;

import static org.jboss.arquillian.ajocado.utils.URLUtils.buildUrl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;

import org.openqa.selenium.support.FindBy;
import org.richfaces.tests.metamer.ftest.AbstractWebDriverTest;
import org.richfaces.tests.page.fragments.impl.calendar.popup.RichFacesCalendarPopupComponent;
import org.richfaces.tests.page.fragments.impl.editor.RichFacesEditor;
import org.richfaces.tests.page.fragments.impl.panel.refactor.RichFacesPanel;
import org.richfaces.tests.page.fragments.impl.panel.refactor.TextualRichFacesPanel;
import org.richfaces.tests.page.fragments.impl.tabPanel.RichFacesTabPanel;
import org.testng.annotations.Test;

public class TestPanelFragmentShowcase extends AbstractWebDriverTest {

    @FindBy(id = "fieldsetHeader")
    private TextualRichFacesPanel textPanelWithHeader;

    @FindBy(id = "fieldsetWithoutHeader")
    private TextualRichFacesPanel textPanelWithoutHeader;

    @FindBy
    private MySuperCustomPanel panelWithCalendarAndEditorInHeader;

    @Override
    public URL getTestUrl() {
        return buildUrl(contextPath, "faces/components/richPanel/simple.xhtml");
    }

    @Test
    public void testPanel() {
        String headerText = textPanelWithHeader.getHeaderContent().getText();
        assertEquals("header of panel", headerText);

        String bodyText = textPanelWithHeader.getBodyContent().getText();
        assertTrue(bodyText.contains("Lorem ipsum"));

        try {
            textPanelWithoutHeader.getHeaderContent().getText();
            fail("Should throw exception as there is no header on that panel!");
        } catch (IllegalStateException ex) {
            // ok
        }

        // just API
        // panelWithCalendarAndEditorInHeader.getBodyContent().getTabPanel();
    }

    public class MySuperCustomPanel extends RichFacesPanel<MySuperHeader, MySuperBody> {

    }

    public class MySuperHeader {

        @FindBy
        RichFacesCalendarPopupComponent calendar;

        @FindBy
        RichFacesEditor editor;

        public RichFacesCalendarPopupComponent getCalendar() {
            return calendar;
        }
    }

    public class MySuperBody {

        @FindBy
        private RichFacesTabPanel tabPanel;

        public RichFacesTabPanel getTabPanel() {
            return tabPanel;
        }
    }
}
