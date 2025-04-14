import static javax.swing.JOptionPane.*;
import static java.lang.Integer.parseInt;
import static java.lang.Double.parseDouble;

public class App {
    private static ListaDupla<Cidade> cidades = new ListaDupla<>();

    public static void main(String[] args) {
        int opcao = 0;
        do {
            String input = showInputDialog(
                "MENU PRINCIPAL\n\n" +
                "1 - Cadastrar cidade\n" +
                "2 - Cadastrar ligação direta\n" +
                "3 - Listar cidades e ligações\n" +
                "4 - Verificar ligação direta e tempo\n" +
                "5 - Buscar ligações por tempo máximo\n" +
                "6 - Sair\n\n" +
                "Informe a opção desejada:"
            );

            if (input == null) {
                int confirmacao = showConfirmDialog(null, "Tem certeza que deseja sair?", "Confirmação", YES_NO_OPTION);
                if (confirmacao == YES_OPTION) {
                    System.exit(0);
                } else {
                    continue;
                }
            }

            try {
                opcao = parseInt(input);
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> cadastrarCidade();
                case 2 -> cadastrarLigacao();
                case 3 -> listarCidades();
                case 4 -> verificarLigacao();
                case 5 -> buscarPorTempo();
                case 6 -> {
                    int confirmacao = showConfirmDialog(null, "Tem certeza que deseja sair?", "Confirmação", YES_NO_OPTION);
                    if (confirmacao == YES_OPTION) {
                        System.exit(0);
                    }
                }
                default -> showMessageDialog(null, "Opção inválida!");
            }
        } while (true);
    }

    private static void cadastrarCidade() {
        String nome = showInputDialog("Digite o nome da cidade:");
        if (nome != null && cidades.buscar(new Cidade(nome)) == null) {
            cidades.inserir(new Cidade(nome));
            showMessageDialog(null, "Cidade cadastrada com sucesso!");
        } else {
            showMessageDialog(null, "Cidade já cadastrada ou nome inválido.");
        }
    }

    private static void cadastrarLigacao() {
        String origem = showInputDialog("Digite o nome da cidade de origem:");
        if (origem == null) return;
        No<Cidade> noOrigem = cidades.buscar(new Cidade(origem));
        if (noOrigem == null) {
            showMessageDialog(null, "Cidade de origem não encontrada.");
            return;
        }

        String destino = showInputDialog("Digite o nome da cidade de destino:");
        if (destino == null) return;

        String distStr = showInputDialog("Informe a distância (km):");
        if (distStr == null) return;
        double distancia = parseDouble(distStr);

        String trafStr = showInputDialog("Informe o fator de tráfego (0 a 2):");
        if (trafStr == null) return;
        double trafego = parseDouble(trafStr);

        String pedStr = showInputDialog("Informe o número de pedágios:");
        if (pedStr == null) return;
        int pedagios = parseInt(pedStr);

        Ligacao ligacao = new Ligacao(destino, distancia, trafego, pedagios);
        noOrigem.getDado().getLigacoes().inserir(ligacao);
        showMessageDialog(null, "Ligação cadastrada com sucesso!");
    }

    private static void listarCidades() {
        No<Cidade> atual = cidades.getPrimeiro();
        StringBuilder sb = new StringBuilder();

        while (atual != null) {
            Cidade cidade = atual.getDado();
            sb.append("Cidade: ").append(cidade.getNome()).append("\n");
            No<Ligacao> l = cidade.getLigacoes().getPrimeiro();
            while (l != null) {
                sb.append(" → ").append(l.getDado().toString()).append("\n");
                l = l.getProximo();
            }
            sb.append("\n");
            atual = atual.getProximo();
        }

        showMessageDialog(null, sb.length() > 0 ? sb.toString() : "Nenhuma cidade cadastrada.");
    }

    private static void verificarLigacao() {
        String origem = showInputDialog("Digite o nome da cidade de origem:");
        if (origem == null) return;
        String destino = showInputDialog("Digite o nome da cidade de destino:");
        if (destino == null) return;

        No<Cidade> noOrigem = cidades.buscar(new Cidade(origem));
        if (noOrigem == null) {
            showMessageDialog(null, "Cidade de origem não encontrada.");
            return;
        }

        ListaDupla<Ligacao> ligacoes = noOrigem.getDado().getLigacoes();
        No<Ligacao> atual = ligacoes.getPrimeiro();
        while (atual != null) {
            if (atual.getDado().getDestino().equalsIgnoreCase(destino)) {
                double tempo = atual.getDado().calcularTempo();
                String tempoFormatado = String.format("%.2f", tempo);
                showMessageDialog(null, "Existe ligação direta.\nTempo estimado: " + tempoFormatado + " minutos.");
                return;
            }
            atual = atual.getProximo();
        }
        showMessageDialog(null, "Não existe ligação direta entre as cidades informadas.");
    }

    private static void buscarPorTempo() {
        String tempoStr = showInputDialog("Digite o tempo máximo (em minutos):");
        if (tempoStr == null) return;
        double tempoMax = parseDouble(tempoStr);

        StringBuilder sb = new StringBuilder("Entregas possíveis com tempo ≤ " + tempoMax + " minutos:\n\n");

        No<Cidade> atual = cidades.getPrimeiro();
        while (atual != null) {
            String origem = atual.getDado().getNome();
            No<Ligacao> lig = atual.getDado().getLigacoes().getPrimeiro();
            while (lig != null) {
                if (lig.getDado().calcularTempo() <= tempoMax) {
                    sb.append(origem).append(" → ").append(lig.getDado().getDestino())
                      .append(" (").append(String.format("%.2f", lig.getDado().calcularTempo())).append(" min)\n");
                }
                lig = lig.getProximo();
            }
            atual = atual.getProximo();
        }

        String resultado = sb.toString();
        showMessageDialog(null, resultado.contains("→") ? resultado : "Nenhuma entrega possível dentro do tempo informado.");
    }
}
