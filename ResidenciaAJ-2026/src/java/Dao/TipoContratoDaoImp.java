package Dao;

import Interface.ITipoContrato;
import Model.TipoContrato;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Util.ConexionSingleton;

public class TipoContratoDaoImp implements ITipoContrato {
    @Override
    public List<TipoContrato> listar() {
        List<TipoContrato> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipo_contrato WHERE estado = '1'";
        try {
            Connection conn = ConexionSingleton.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TipoContrato t = new TipoContrato();
                t.setId_tipoContrato(rs.getInt("id_tipoContrato"));
                t.setNombre(rs.getString("nombre"));
                t.setDias_duracion(rs.getInt("dias_duracion"));
                t.setEstado(rs.getString("estado"));
                lista.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
