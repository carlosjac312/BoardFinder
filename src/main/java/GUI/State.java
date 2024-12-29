package GUI;

public enum State {
    LOGIN("Login"), HOME("Home"), SEARCH("Search"), CREATE("Create");

    private String valor; // Atributo de cada constante

    State(String valor) { // Constructor
        this.valor = valor;
    }

    public String getValor() { // Método
        return valor;
    }
}
