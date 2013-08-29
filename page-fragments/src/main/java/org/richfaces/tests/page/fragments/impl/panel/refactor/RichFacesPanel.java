package org.richfaces.tests.page.fragments.impl.panel.refactor;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jodah.typetools.TypeResolver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class RichFacesPanel<HEADER, BODY> extends Panel<HEADER, BODY> {

    @FindBy(css = "div.rf-p-hdr")
    private WebElement header;

    @FindBy(css = "div.rf-p-b")
    private WebElement body;

    @Drone
    private WebDriver browser;

    @Override
    @SuppressWarnings("unchecked")
    public HEADER getHeaderContent() {
        if (!new WebElementConditionFactory(header).isPresent().apply(browser)) {
            throw new IllegalStateException("You are trying to get header content of the panel which does not have header!");
        }
        Class<HEADER> containerClass = (Class<HEADER>) TypeResolver.resolveRawArguments(Panel.class, getClass())[0];
        return Graphene.createPageFragment(containerClass, header);
    }

    @Override
    @SuppressWarnings("unchecked")
    public BODY getBodyContent() {
        Class<BODY> containerClass = (Class<BODY>) TypeResolver.resolveRawArguments(Panel.class, getClass())[1];
        return Graphene.createPageFragment(containerClass, body);
    }
}