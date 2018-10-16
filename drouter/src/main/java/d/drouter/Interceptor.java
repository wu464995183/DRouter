package d.drouter;

public interface Interceptor {
    RouterResponse intercept(RouterChain chain);
}
