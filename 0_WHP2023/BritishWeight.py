class BritishWeight:
    _pounds: int = 0

    def __init__(self, pounds=0, stones=0):
        self._pounds = int(stones) * 14 + int(pounds)

    def __add__(self, other):
        if isinstance(other, int):
            return BritishWeight(self._pounds + other)

        if isinstance(other, BritishWeight):
            return BritishWeight(self._pounds + other._pounds)

    def __radd__(self, other):
        return self + other

    def __str__(self) -> str:
        return str(self._pounds) + "lb = " + str(self._pounds//14) + "st " + str(self._pounds%14) + "lb"


brit = BritishWeight(24, 1)
print(brit)
print(brit + 10)
print(10 + brit)