public class ListaDupla<T> {
    private No<T> primeiro;
    private No<T> ultimo;
    private int tamanho;

    public void inserir(T elemento) {
        No<T> novo = new No<>(elemento);
        if (primeiro == null) {
            primeiro = novo;
            ultimo = novo;
        } else {
            ultimo.setProximo(novo);
            novo.setAnterior(ultimo);
            ultimo = novo;
        }
        tamanho++;
    }

    public boolean remover(T elemento) {
        No<T> atual = primeiro;
        while (atual != null) {
            if (atual.getDado().equals(elemento)) {
                if (atual == primeiro) {
                    primeiro = atual.getProximo();
                    if (primeiro != null) primeiro.setAnterior(null);
                } else if (atual == ultimo) {
                    ultimo = atual.getAnterior();
                    if (ultimo != null) ultimo.setProximo(null);
                } else {
                    atual.getAnterior().setProximo(atual.getProximo());
                    atual.getProximo().setAnterior(atual.getAnterior());
                }
                tamanho--;
                return true;
            }
            atual = atual.getProximo();
        }
        return false;
    }

    public No<T> buscar(T elemento) {
        No<T> atual = primeiro;
        while (atual != null) {
            if (atual.getDado().equals(elemento)) {
                return atual;
            }
            atual = atual.getProximo();
        }
        return null;
    }

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public int getTamanho() {
        return tamanho;
    }

    public No<T> getPrimeiro() {
        return primeiro;
    }

    public No<T> getUltimo() {
        return ultimo;
    }
}