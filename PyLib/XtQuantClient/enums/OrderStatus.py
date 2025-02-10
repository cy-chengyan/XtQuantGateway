
from enum import IntEnum


class OrderStatus(IntEnum):
    """定单状态"""

    LOCAL_SAVED = -1
    """保存在本地"""
    SUBMIT_FAILED = -2
    """本地向QMT下单提交失败"""
    SUBMITTING = -3
    """本地正在向QMT下单中"""
    SUBMIT_CANCELING = -4
    """本地正在向QMT提交撤单中"""
    UNKNOWN = 255
    """未知"""
    UNREPORTED = 48
    """未报"""
    WAIT_REPORTING = 49
    """待报"""
    REPORTED = 50
    """已报"""
    REPORTED_CANCEL = 51
    """已报待撤"""
    PARTSUCC_CANCEL = 52
    """部成待撤"""
    PART_CANCEL = 53
    """部撤"""
    CANCELED = 54
    """已撤"""
    PART_SUCC = 55
    """部成"""
    SUCCEEDED = 56
    """已成"""
    JUNK = 57
    """废单"""
