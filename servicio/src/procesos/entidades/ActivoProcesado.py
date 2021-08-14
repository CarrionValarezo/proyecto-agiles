from src.login import Administrador
from src.procesos.entidades.Activo import Activo


class ActivoProcesado(Activo):

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.revision: bool = kwargs.get("revision_activo")
        self.estado: str = kwargs.get("estado_revision_activo")
        self.observacion: str = kwargs.get("observacion_revision")
        self.revisor: Administrador = kwargs.get("revisor")

    def to_dict(self) -> dict:
        return {**super().to_dict(),
                "revision_activo": self.revision,
                "estado_revision_activo": self.estado,
                "observacion_revision": self.observacion,
                "revisor": self.revisor.to_dict()}

