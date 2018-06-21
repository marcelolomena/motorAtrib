package cl.motoratrib.rest.jsrules.service;

import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpGetReglaIN;
import cl.bancochile.centronegocios.controldelimites.persistencia.domain.SpGetReglaOUT;
import cl.bancochile.centronegocios.controldelimites.persistencia.repository.SpGetReglaDAO;
import cl.bancochile.plataformabase.error.PlataformaBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleServiceImpl implements RuleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleServiceImpl.class);

    private static final String CODIGO_ERROR_GENERICO = "100000";
    private static final String GLOSA_ERROR_GENERICO = "Error al calcular la regla ";


    @Autowired
    SpGetReglaDAO spGetReglaDAO;

    @Override
    public SpGetReglaOUT getRuleByName(String name) throws PlataformaBaseException {
        SpGetReglaOUT ruleValue;
        try {
            SpGetReglaIN params = new SpGetReglaIN();
            params.setPNombre(name);
            ruleValue=this.spGetReglaDAO.execute(params);
            LOGGER.debug(ruleValue.toString());
        }catch(Exception e){
            throw new PlataformaBaseException(GLOSA_ERROR_GENERICO, e, CODIGO_ERROR_GENERICO);
        }
        return ruleValue;
    }
}