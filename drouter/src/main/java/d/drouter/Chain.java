package d.drouter;

public interface Chain {
    RouterResponse process(RouterRequest request);
}
