package model

import anorm.RowParser

/**
 * Created by arnaud.deprez on 4/08/15.
 */
case class StockItem(id: Long,
                     productId: Long,
                     warehouseId: Long,
                     quantity: Long)

object StockItem {
  val stockItemParser: RowParser[StockItem] = {
    import anorm.SqlParser._
    import anorm.~

    long("id") ~
    long("product_id") ~
    long("warehouse_id") ~
    long("quantity") map {
      case id ~ productId ~ warehouseId ~ quantity => StockItem(id, productId, warehouseId, quantity)
    }
  }
}
