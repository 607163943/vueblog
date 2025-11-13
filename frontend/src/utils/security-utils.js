import CryptoJS from 'crypto-js'

// 将传入的明文密码进行加密并返回
export const encryptPassword = (orgPassword) => {
  // 得到一个哈希对象
  const hash = CryptoJS.SHA256(orgPassword)
  return hash.toString(CryptoJS.enc.Hex) // 转成 16 进制字符串
}
