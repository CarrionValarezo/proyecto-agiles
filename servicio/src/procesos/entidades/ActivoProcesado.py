from src.login import Administrador
from src.procesos.entidades.Activo import Activo


class ActivoProcesado(Activo):

    OBSERVACION: str = "OBSERVACION"
    REVISADO: int = 1

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.validacion: bool = kwargs.get("revision_activo")
        self.estado_validacion: str = kwargs.get("estado_revision_activo")
        self.observacion: str = kwargs.get("observacion_revision")
        self.revisor: Administrador = kwargs.get("revisor")

    def to_dict(self) -> dict:
        return {**super().to_dict(),
                "revision_activo": self.validacion,
                "estado_revision_activo": self.estado_validacion,
                "observacion_revision": self.observacion,
                "revisor": self.revisor.to_dict()}

    def esta_revisado(self) -> bool:
        return True if self.validacion == self.REVISADO else False

    def tiene_observacion(self) -> bool:
        return True if self.estado_validacion == self.OBSERVACION else False

