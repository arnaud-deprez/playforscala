package models


/**
 * Created by arnaud.deprez on 16/08/15.
 */
//case class Product(ean: Long, name: String, description: Option[String])
case class Product(ean: Long, name: String, description: Option[String],
                   pieces: Option[Int], manufacturer: Company,
                   tags: List[String], active: Boolean)

object Product {

  val typeSafe = Company("TypeSafe", Contact(email = Some("typesafe@typesafe.com"), None, None))
  val atos = Company("Atos", Contact(email = Some("atos@atos.net"), None, None))
  val microsoft = Company("Microsoft", Contact(email = Some("microsoft@microsoft.com"), None, None))

  var products = Set(
    Product(5010255079763L, "Paperclips Large", Some("Large Plain Pack of 1000"),
      Some(5), typeSafe, Nil, true),
    Product(5018206244666L, "Giant Paperclips", Some("Giant Plain 51mm 100 pack"),
      Some(13), atos, Nil, true),
    Product(5018306332812L, "Paperclip Giant Plain", Some("Giant Plain Pack of 10000"),
      Some(21), microsoft, Nil, true),
    Product(5018306312913L, "No Tear Paper Clip", Some("No Tear Extra Large Pack of 1000"),
      Some(8), typeSafe, Nil, true),
    Product(5018206244611L, "Zebra Paperclips", Some("Zebra Length 28mm Assorted 150 Pack"),
      Some(15), typeSafe, Nil, true)
  )

  def findAll = this.products.toList.sortBy(_.ean)

  def findByEan(ean: Long) = this.products.find(_.ean == ean)

  def update(product: Product) = {
    findByEan(product.ean).map(oldProduct => {
      this.products = this.products - oldProduct + product
      Right(product)
    }).getOrElse(Left("Product not found"))
  }

  def save(product: Product) = update(product).fold(a => a, p => this.products = this.products + p)

  def delete(product: Product): Unit = findByEan(product.ean).
    map(p => this.products = this.products - p).
    orElse(throw new IllegalArgumentException("Product Not Found!"))
}
