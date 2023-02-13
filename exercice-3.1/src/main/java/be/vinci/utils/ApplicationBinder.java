package be.vinci.utils;

import be.vinci.domain.DomainFactory;
import be.vinci.domain.DomainFactoryImpl;
import be.vinci.services.PageDataService;
import be.vinci.services.PageDataServiceImpl;
import be.vinci.services.UserDataService;
import be.vinci.services.UserDataServiceImpl;
import jakarta.inject.Singleton;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(DomainFactoryImpl.class).to(DomainFactory.class).in(Singleton.class);
        bind(PageDataServiceImpl.class).to(PageDataService.class).in(Singleton.class);
        bind(UserDataServiceImpl.class).to(UserDataService.class).in(Singleton.class);
    }
}
