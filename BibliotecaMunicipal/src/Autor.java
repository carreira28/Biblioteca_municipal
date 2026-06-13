import java.time.LocalDate;

public class Autor {

    private int id;
    private String nome;
    private LocalDate dataNascimento;

    public Autor(int id, String nome, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public LocalDate getDataNascimento() { return dataNascimento; }

    @Override
    public String toString() {
        String data = dataNascimento != null ? dataNascimento.toString() : "Desconhecida";
        return String.format("[%d] Nome: %s | Data Nascimento: %s", id, nome, data);
    }
}