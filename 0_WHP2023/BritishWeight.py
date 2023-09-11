class BritishWeight:
    _pounds: int = 0

    def __init__(self, pounds=0, stones=0):
        """
        constructor
        automatically calculates stones from pounds
        :param pounds: pounds
        :param stones: 14 pounds
        """
        self._pounds = int(stones) * 14 + int(pounds)

    def __add__(self, other):
        """
        adds either two BritishWeights
        or a BritishWeight with an int
        :param other:
        :return:
        """
        if isinstance(other, int):
            return BritishWeight(self._pounds + other)

        if isinstance(other, BritishWeight):
            return BritishWeight(self._pounds + other._pounds)

    def __radd__(self, other):
        """
        implements right add
        :param other:
        :return:
        """
        return self + other

    def __str__(self) -> str:
        """
        toString method
        :return:
        """
        return str(self._pounds) + "lb = " + str(self._pounds//14) + "st " + str(self._pounds%14) + "lb"


brit = BritishWeight(24, 1)
print(brit)
print(brit + 10)
print(10 + brit)