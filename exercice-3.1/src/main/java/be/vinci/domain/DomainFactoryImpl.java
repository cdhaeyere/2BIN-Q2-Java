package be.vinci.domain;

public class DomainFactoryImpl implements DomainFactory {

    @Override
    public User createUser() {
        return new UserImpl();
    }

    @Override
    public Page createPage() {
        return new PageImpl();
    }
}
