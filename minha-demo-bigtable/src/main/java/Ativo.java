public class Ativo {

    public Ativo(String ativo, String data, String alta, String baixa, String abertura, String fechamento, String volume, String fechamentoAjustado) {
        this.ativo = ativo;
        this.data = data;
        this.alta = alta;
        this.baixa = baixa;
        this.abertura = abertura;
        this.fechamento = fechamento;
        this.volume = volume;
        this.fechamentoAjustado = fechamentoAjustado;
    }

    private String ativo, data, alta, baixa, abertura, fechamento, volume, fechamentoAjustado;

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAlta() {
        return alta;
    }

    public void setAlta(String alta) {
        this.alta = alta;
    }

    public String getBaixa() {
        return baixa;
    }

    public void setBaixa(String baixa) {
        this.baixa = baixa;
    }

    public String getAbertura() {
        return abertura;
    }

    public void setAbertura(String abertura) {
        this.abertura = abertura;
    }

    public String getFechamento() {
        return fechamento;
    }

    public void setFechamento(String fechamento) {
        this.fechamento = fechamento;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getFechamentoAjustado() {
        return fechamentoAjustado;
    }

    public void setFechamentoAjustado(String fechamentoAjustado) {
        this.fechamentoAjustado = fechamentoAjustado;
    }
}
