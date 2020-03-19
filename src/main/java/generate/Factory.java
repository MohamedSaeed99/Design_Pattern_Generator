package generate;

//Abstract Factory that would instantiate and returns the specified design pattern
public class Factory implements DesignFactory{
    @Override
    public Pattern designPattern(String pat){
        switch(pat.toLowerCase()){
            case "abstract_factory":
                return new AbstractFactory();
            case "builder":
                return new Builder();
            case "facade":
                return new Facade();
            case "factory_method":
                return new FactoryMethod();
            case "chain_of_responsibility":
                return new ChainOfResponsibility();
            case "visitor":
                return new Visitor();
            case "template_method":
                return new TemplateMethod();
            case "mediator":
                return new Mediator();
            default:
                return null;
        }
    }
}
