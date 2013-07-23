package org.richfaces.tests.page.fragments.impl.accordion.refactor;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;

public class RichFacesAccordionItem implements ComponentContainer {

    @Root
    private WebElement root;

    @Override
    public <T> T getContent(Class<T> clazz) {
        return Graphene.createPageFragment(clazz, root);
    }
}
