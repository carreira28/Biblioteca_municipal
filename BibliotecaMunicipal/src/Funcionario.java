public class Funcionario {

    private int id;
    private String nome;
    private String localidade;

    public Funcionario(int id, String nome, String localidade) {
        this.id = id;
        this.nome = nome;
        this.localidade = localidade;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getLocalidade() { return localidade; }

    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        return String.format("[%d] %-30s | %s", id, nome, localidade);
    }
}