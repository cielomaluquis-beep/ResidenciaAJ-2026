package Util;

import Dao.AlertaDaoImpl;
import Dao.ContratoDaoImp;
import Dao.ReservaDaoImpl;
import Dao.UsuarioDaoImpl;
import Interface.IAlerta;
import Interface.IContrato;
import Interface.IReserva;
import Interface.IUsuario;
import Model.Alerta;
import Model.Contrato;
import Model.Reserva;
import Model.Usuario;
import java.util.List;

public class AlertaService {

    public static void evaluarAlertas() {
        IAlerta alertaDao = new AlertaDaoImpl();
        IContrato contratoDao = new ContratoDaoImp();
        IReserva reservaDao = new ReservaDaoImpl();
        IUsuario usuarioDao = new UsuarioDaoImpl();

       
        List<Contrato> contratos = contratoDao.listar(); // Asumiendo que existe o adaptaremos
        
        long hoy = System.currentTimeMillis();
        long sieteDias = 7L * 24 * 60 * 60 * 1000;
        
        // Obtener usuario gerente y administradora para enviarles alertas
        List<Usuario> admins = usuarioDao.lista();
        int idAdmin = -1;
        int idGerente = -1;
        for (Usuario u : admins) {
            if (u.getId_rol() == 1) idGerente = u.getId_usuario();
            if (u.getId_rol() == 2) idAdmin = u.getId_usuario();
        }

        for (Contrato c : contratos) {
            if ("Activo".equalsIgnoreCase(c.getEstado())) {
                long diff = c.getFecha_fin().getTime() - hoy;
                
                // Vencimiento en 7 dias
                if (diff > 0 && diff <= sieteDias) {
                    String msjEstudiante = "Tu contrato vence el " + c.getFecha_fin().toString() + ". Por favor renueva tu alquiler.";
                    if (!alertaDao.existeAlertaReciente(c.getId_usuario(), "Vencimiento Contrato", msjEstudiante)) {
                        Alerta aEst = new Alerta();
                        aEst.setTipo_alerta("Vencimiento Contrato");
                        aEst.setMensaje(msjEstudiante);
                        aEst.setId_usuario(c.getId_usuario());
                        alertaDao.insert(aEst);
                        
                        if (idAdmin != -1) {
                            Alerta aAdm = new Alerta();
                            aAdm.setTipo_alerta("Vencimiento Contrato");
                            aAdm.setMensaje("El contrato del residente ID " + c.getId_usuario() + " vence el " + c.getFecha_fin().toString());
                            aAdm.setId_usuario(idAdmin);
                            alertaDao.insert(aAdm);
                        }
                    }
                } 
                
                else if (diff <= 0) {
                    String msjGerente = "El contrato del residente ID " + c.getId_usuario() + " ha VENCIDO y no ha sido renovado.";
                    if (!alertaDao.existeAlertaReciente(idGerente, "Contrato Vencido", msjGerente)) {
                        if (idGerente != -1) {
                            Alerta aGer = new Alerta();
                            aGer.setTipo_alerta("Contrato Vencido");
                            aGer.setMensaje(msjGerente);
                            aGer.setId_usuario(idGerente);
                            alertaDao.insert(aGer);
                        }
                        if (idAdmin != -1) {
                            Alerta aAdm = new Alerta();
                            aAdm.setTipo_alerta("Contrato Vencido");
                            aAdm.setMensaje(msjGerente);
                            aAdm.setId_usuario(idAdmin);
                            alertaDao.insert(aAdm);
                        }
                        
                        // Cambiamos estado de contrato en BD si es necesario
                        contratoDao.actualizarEstado(c.getId_contrato(), "Vencido");
                    }
                }
            }
        }

        List<Reserva> reservas = reservaDao.listarTodas();
        for (Reserva r : reservas) {
            // RN-12: Cancelación de reservas no confirmadas o sin pago tras 3 días (72 hrs)
            if ("1".equals(r.getEstado()) || "Pendiente".equalsIgnoreCase(r.getEstado())) {
                long diffReserva = hoy - r.getFecha_reserva().getTime();
                long tresDias = 3L * 24 * 60 * 60 * 1000;
                
                if (diffReserva >= tresDias) {
                    // Cancelar la reserva automáticamente
                    reservaDao.actualizarEstado(r.getId_reserva(), "0"); // 0 = Rechazada/Cancelada
                    
                    String msjCancel = "Tu reserva de la habitación " + r.getId_habitacion() + " ha sido cancelada por falta de pago o confirmación luego de 3 días.";
                    if (!alertaDao.existeAlertaReciente(r.getId_usuario(), "Reserva Cancelada", msjCancel)) {
                        Alerta a = new Alerta();
                        a.setTipo_alerta("Reserva Cancelada");
                        a.setMensaje(msjCancel);
                        a.setId_usuario(r.getId_usuario());
                        alertaDao.insert(a);
                    }
                    continue; // Ya fue cancelada, no evaluar saldos
                }
            }

            if ("Activo".equalsIgnoreCase(r.getEstado()) || "2".equals(r.getEstado())) {
                double montoAlquiler = 400.0; // Simulacion de monto mensual de habitacion (idealmente desde DB)
                double pagado = r.getPagoMonto() != null ? r.getPagoMonto() : 0.0;
                double saldoPendiente = montoAlquiler - pagado;

                if (saldoPendiente > 0) {
                    String msjSaldo = "Tienes un saldo pendiente de S/ " + saldoPendiente + " en tu habitación.";
                    if (!alertaDao.existeAlertaReciente(r.getId_usuario(), "Saldo Pendiente", msjSaldo)) {
                        Alerta a = new Alerta();
                        a.setTipo_alerta("Saldo Pendiente");
                        a.setMensaje(msjSaldo);
                        a.setId_usuario(r.getId_usuario());
                        alertaDao.insert(a);
                    }
                }
            }
        }
    }
}
