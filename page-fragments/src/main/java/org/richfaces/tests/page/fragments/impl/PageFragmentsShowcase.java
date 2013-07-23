package org.richfaces.tests.page.fragments.impl;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.richfaces.tests.page.fragments.impl.accordion.refactor.RichFacesAccordion;
import org.richfaces.tests.page.fragments.impl.accordion.refactor.RichFacesAccordionItem;
import org.richfaces.tests.page.fragments.impl.contextMenu.RichFacesContextMenu;

public class PageFragmentsShowcase {

    @FindBy(jquery = "blah:eq(1)")
    private RichFacesAccordion accordion;

    @Test
    public void testFoo() {
        RichFacesAccordionItem firstAccordionPanel = accordion.switchTo(1);
        RichFacesContextMenu contextMenu = firstAccordionPanel.getContent(FirstAccordion.class).getContextMenu();
        contextMenu.invoke();
    }

    public class FirstAccordion {

        @FindBy(jquery = "foo:bar")
        private RichFacesContextMenu contextMenu;

        @FindBy(css = ".class")
        private WebElement text;

        public RichFacesContextMenu getContextMenu() {
            return contextMenu;
        }

        public WebElement getText() {
            return text;
        }
    }
}
