public class Requisitante {

    private int    id;
    private String nome;
    private String contacto;
    private String localidade;

    public Requisitante(int id, String nome, String contacto, String localidade) {
        this.id         = id;
        this.nome       = nome;
        this.contacto   = contacto;
        this.localidade = localidade;
    }

    public int    getId()         { return id; }
    public String getNome()       { return nome; }
    public String getContacto()   { return contacto; }
    public String getLocalidade() { return localidade; }

    public void setNome(String nome)         { this.nome     = nome;     }
    public void setContacto(String contacto) { this.contacto = contacto; }

    @Override
    public String toString() {
        return String.format("[%d] %-30s | Tel: %-15s | %s",
                id, nome, contacto, localidade);
    }
}